package com.kiolis.optimizer.menu.data;

import com.google.gson.GsonBuilder;
import com.kiolis.optimizer.menu.models.Substance;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Logger;

/**
 * Created by kiolis on 26/05/15.
 */
public class MyCoachRecipeFetcherImpl implements RecipeFetcher {

  private GsonBuilder builder;
  private DB recipeDb;
  private ConcurrentNavigableMap<String, Substance> dbMap;

  Logger logger = Logger.getLogger(MyCoachRecipeFetcherImpl.class.getName());

  public MyCoachRecipeFetcherImpl() {
	builder = new GsonBuilder();
	recipeDb = DBMaker.newFileDB(new File("recipeDb"))
			.closeOnJvmShutdown()
			.make();
	dbMap = recipeDb.getTreeMap("recipes");
  }

  @Override
  public void fetch(List<Substance> substances) throws UnirestException {

	ConcurrentNavigableMap<String, Substance> dbMap = getDbMap();
	if (dbMap.isEmpty()) {
	  String href = "http://85.10.209.248:8080/mycoachnutrition/api/recipes?page=1&window=20";
	  System.out.println("Start retrieving recipes from MCN and store them in the local DB...");
	  do {
		String responseAsJson = executeRequest(href);
		processResponse(substances, responseAsJson);
		int numberOfRecipes = substances.size();
		Object object = getBuilder().create().fromJson(responseAsJson, Object.class);
		int total = (int) ((Double) getValueForKey(object, "total")).doubleValue();
		int percent = (int) Math.ceil(((float) substances.size() / (float) total * 100));
		printProgressBar(percent, numberOfRecipes, "recipes");
		Object hrefObject = getNextHref(object);
		if (hrefObject != null) {
		  href = (String) getNextHref(object);
		} else {
		  href = null;
		  printProgressBar(100, numberOfRecipes, "recipes");
		}
	  } while (href instanceof String);
	  System.out.println("");
	  System.out.println("Done retrieving all recipes!");
	  System.out.println("");
	  recipeDb.commit();
	  recipeDb.close();
	} else {
	  Collection<Substance> recipes = dbMap.values();
	  substances.addAll(recipes);
	}
  }

  public static void printProgressBar(int percent, int numberOfItems, String itemsLabel) {
	StringBuilder bar = new StringBuilder("[");
	for (int i = 0; i < 50; i++) {
	  if (i < (percent / 2)) {
		bar.append("=");
	  } else if (i == (percent / 2)) {
		bar.append(">");
	  } else {
		bar.append(" ");
	  }
	}
	bar.append("]   " + percent + "%     " + numberOfItems + " " + itemsLabel);
	System.out.print("\r" + bar.toString());
  }

  private String executeRequest(String url) throws UnirestException {
	HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
			.header("accept", "application/json")
			.header("X-AUTH-TOKEN", "eyJpZCI6Nywicm9sZXMiOlt7ImlkIjoxLCJuYW1lIjoiUk9MRV9VU0VSIn0seyJpZCI6MiwibmFtZSI6IlJPTEVfQURNSU4ifSx7ImlkIjozLCJuYW1lIjoiUk9MRV9SRUNJUEVfVkFMSURBVE9SIn0seyJpZCI6NCwibmFtZSI6IlJPTEVfTlVUUklUSU9OSVNUIn1dLCJwc2V1ZG8iOiJtaWNoZWwiLCJlbWFpbCI6InJlZG1pbmVhdGtpb2xpc0BnbWFpbC5jb20iLCJhY3RpdmUiOnRydWUsInJlZ2lzdHJhdGlvbiI6dHJ1ZSwiZW5jb2RlZFBhc3N3b3JkIjoiNjMwYWE1MjZhYWI4N2Y3YWUxYmIzMTYyYWZhZDZhNTBkMmMyYmM0OWY1YTJkOTU3ZGY4YzNmZGEwMjQ1NWI2YjcwZTI4YzI0NzAzNTUwZWQiLCJyZWdpc3RyYXRpb25JZCI6bnVsbCwiZXhwaXJlcyI6MTQzNDcwODMyMjM1OH0=.ZTI8ZEOF8ugsK0/rZi2hMwlRlxi2nueB6hY7KUuIY9g=")
			.asJson();
	return jsonResponse.getBody().toString();
  }

  private void processResponse(List<Substance> substances, String json) {

	ConcurrentNavigableMap<String, Substance> dbMap = getDbMap();

	Object object = getBuilder().create().fromJson(json, Object.class);
	List resources = getListForKey(object, "resources");
	for (Object resource : resources) {
	  String id = (String) getValueForKey(resource, "id");

	  Object properties = getMapForKey(resource, "properties");

	  String title = (String) getValueForKey(properties, "title");

	  String mealTypeCode = getMealTypeCode(properties);

	  Substance substance = new Substance(id);
	  substance.setLabel(title);

	  for (Substance.Type type : Substance.Type.values()) {
		if (mealTypeCode != null && mealTypeCode.equals(type.getCode())) {
		  substance.setType(type);
		}
	  }

	  Map classification = getMapForKey(properties, "classification");
	  for (Category category : Category.values()) {
		String categoryName = category.name();
		Double value = (Double) classification.get(categoryName);
		if (category != null) {
		  switch (category) {
			case Fruit:
			  substance.setFruits(getValue(value));
			  break;
			case Legumes:
			  substance.setVegetables(getValue(value));
			  break;
			case Feculents:
			  substance.setStarches(getValue(value));
			  break;
			case ViandePoissonOeuf:
			  substance.setMeatFishEggs(getValue(value));
			  break;
			case Laitage:
			  substance.setDairy(getValue(value));
			  break;
		  }
		}
	  }
	  if (substance.getType() != null) {
		substances.add(substance);
		dbMap.put(id, substance);
	  }
	}
  }

  private String getMealTypeCode(Object resource) {
	Map mealType = getMapForKey(resource, "mealType");
	if (mealType != null) {
	  return (String) mealType.get("code");
	}
	return null;
  }

  private double getValue(Double value) {
	if (value != null) {
	  return value.doubleValue();
	}
	return 0;
  }


  private Object getNextHref(Object object) {
	Map linksMap = getMapForKey(object, "links");
	if (linksMap != null) {
	  Map nextMap = getMapForKey(linksMap, "next");
	  if (nextMap != null) {
		return getValueForKey(nextMap, "href");
	  }
	}
	return null;
  }

  private Map getMapForKey(Object object, String key) {
	if (object instanceof Map) {
	  Map map = (Map) object;
	  Object anObject = map.get(key);
	  Map aMap = getMap(anObject);
	  return aMap;
	}
	return null;
  }

  private List getListForKey(Object object, String key) {
	if (object instanceof Map) {
	  Map map = (Map) object;
	  Object anObject = map.get(key);
	  List list = getList(anObject);
	  return list;
	}
	return null;
  }

  private Object getValueForKey(Object object, String key) {
	if (object instanceof Map) {
	  Map map = (Map) object;
	  return map.get(key);
	}
	return null;
  }

  private Map getMap(Object object) {
	if (object instanceof Map) {
	  return (Map) object;
	}
	return null;
  }

  private List getList(Object object) {
	if (object instanceof List) {
	  return (List) object;
	}
	return null;
  }

  public GsonBuilder getBuilder() {
	return builder;
  }

  public ConcurrentNavigableMap<String, Substance> getDbMap() {
	return dbMap;
  }

}
