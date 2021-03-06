package com.kiolis.optimizer.menu.models;

import com.google.inject.Inject;
import com.kiolis.optimizer.menu.data.MyCoachRecipeFetcherImpl;
import com.kiolis.optimizer.menu.data.RecipeFetcher;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.opt4j.core.start.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author dimitri
 * @version 0.1
 * @since 13/11/14
 * Copyright (c) 2014 Kiolis Software France
 */
public class MenuProblem {

  protected List<Substance> substances = new ArrayList<Substance>();

  protected List<Substance> starters = new ArrayList<Substance>();
  protected List<Substance> mains = new ArrayList<Substance>();
  protected List<Substance> desserts = new ArrayList<Substance>();

  private RecipeFetcher recipeFetcher;

  @Inject
  public MenuProblem(@Constant(value = "size") int size) {
	//createNewRandomSubstances(size);

	recipeFetcher = new MyCoachRecipeFetcherImpl();
	List<Substance> substances = new ArrayList<>();
	try {
	  recipeFetcher.fetch(substances);
	  this.substances = substances;
	} catch (UnirestException e) {
	  e.printStackTrace();
	}

	starters = getSubstancesForType(Substance.Type.STARTER);
	mains = getSubstancesForType(Substance.Type.MAIN);
	desserts = getSubstancesForType(Substance.Type.DESSERT);
  }

  public int getIndex(Substance substance) {
	return substances.indexOf(substance);
  }

  public List<Substance> getSubstancesForType(Substance.Type type) {
	List<Substance> result = new ArrayList<Substance>();
	List<Substance> substances = getSubstances();
	for (Substance substance : substances) {
	  if (substance.getType() == type) {
		result.add(substance);
	  }
	}
	return result;
  }

  public List<Substance> getSubstances() {
	return this.substances;
  }

  public List<Substance> getStarters() {
	return starters;
  }

  public List<Substance> getMains() {
	return mains;
  }

  public List<Substance> getDesserts() {
	return desserts;
  }

  private List<Substance> createNewRandomSubstances(int size) {
	for (int i = 0; i < size; i++) {
	  // fruits -> 360g per day
	  // dairy  -> 250g per day
	  // starches  -> 400g per day
	  final double fruits = generateRandom(0, 500) / 6;
	  final double dairy = generateRandom(0, 400) / 6;
	  final double starches = generateRandom(0, 900) / 6;

	  Substance substance = new Substance(String.valueOf(i));
	  substance.setDairy(dairy);
	  substance.setFruits(fruits);
	  substance.setStarches(starches);
	  substance.setMeatFishEggs(0);
	  substance.setVegetables(0);
	  substance.setType(getRandomType());
	  substances.add(substance);
	}
	return substances;
  }

  private Substance.Type getRandomType() {
	Random random = new Random();
	return Substance.Type.values()[random.nextInt(3)];
  }

  private double generateRandom(int min, int max) {
	double random = min + (Math.random() * ((max - min) + 1));
	return random;
  }
}
