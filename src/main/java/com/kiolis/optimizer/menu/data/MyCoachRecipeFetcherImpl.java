package com.kiolis.optimizer.menu.data;

import com.google.gson.GsonBuilder;
import com.kiolis.optimizer.menu.models.Substance;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by kiolis on 26/05/15.
 */
public class MyCoachRecipeFetcherImpl implements RecipeFetcher {

  private GsonBuilder builder;

  Logger logger = Logger.getLogger(MyCoachRecipeFetcherImpl.class.getName());

  @Override
  public void fetch(List<Substance> substances) throws UnirestException {
	builder = new GsonBuilder();
	String href = "http://85.10.209.248:8080/mycoachnutrition/api/recipes?page=1&window=20";
	int counter = 1;
	do {
	  String responseAsJson = executeRequest(href);
	  System.out.println("Retrieving the page " + counter + "..., we have " + substances.size()  + " recipes");
	  processResponse(substances, responseAsJson);
	  Object object = getBuilder().create().fromJson(responseAsJson, Object.class);
	  Object hrefObject = getNextHref(object);
	  if (hrefObject != null) {
		href = (String) getNextHref(object);
	  }
	  counter++;
	} while ((href instanceof String) && substances.size() < 100);
	System.out.println("Done retrieving all recipes.");
  }

  private String executeRequest(String url) throws UnirestException {
	HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
			.header("accept", "application/json")
			.header("X-AUTH-TOKEN", "eyJpZCI6Nywicm9sZXMiOlt7ImlkIjoxLCJuYW1lIjoiUk9MRV9VU0VSIn0seyJpZCI6MiwibmFtZSI6IlJPTEVfQURNSU4ifSx7ImlkIjozLCJuYW1lIjoiUk9MRV9SRUNJUEVfVkFMSURBVE9SIn0seyJpZCI6NCwibmFtZSI6IlJPTEVfTlVUUklUSU9OSVNUIn1dLCJwc2V1ZG8iOiJtaWNoZWwiLCJlbWFpbCI6InJlZG1pbmVhdGtpb2xpc0BnbWFpbC5jb20iLCJhY3RpdmUiOnRydWUsInJlZ2lzdHJhdGlvbiI6dHJ1ZSwiZW5jb2RlZFBhc3N3b3JkIjoiNjMwYWE1MjZhYWI4N2Y3YWUxYmIzMTYyYWZhZDZhNTBkMmMyYmM0OWY1YTJkOTU3ZGY4YzNmZGEwMjQ1NWI2YjcwZTI4YzI0NzAzNTUwZWQiLCJyZWdpc3RyYXRpb25JZCI6bnVsbCwiZXhwaXJlcyI6MTQzNDcwODMyMjM1OH0=.ZTI8ZEOF8ugsK0/rZi2hMwlRlxi2nueB6hY7KUuIY9g=")
			.asJson();
	return jsonResponse.getBody().toString();
  }

  private void processResponse(List<Substance> substances, String json) {
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
	  if(substance.getType() != null){
		substances.add(substance);
	  }
	}
  }

  private String getMealTypeCode(Object resource) {
	Map mealType = getMapForKey(resource, "mealType");
	if(mealType != null){
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

}
