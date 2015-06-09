package com.kiolis.optimizer.menu;

import com.kiolis.optimizer.menu.models.Menu;
import com.kiolis.optimizer.menu.models.Substance;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class MenuEvaluator implements Evaluator<Menu> {

  @Override
  public Objectives evaluate(Menu phenotype) {

	double fruits = 0;
	double dairy = 0;
	double starches = 0;
	double vegetables = 0;
	double meatFishEggs = 0;

	for (int i = 0; i < phenotype.size(); i++) {
	  fruits += phenotype.get(i).getFruits();
	  dairy += phenotype.get(i).getDairy();
	  starches += phenotype.get(i).getStarches();
	  vegetables += phenotype.get(i).getVegetables();
	  meatFishEggs += phenotype.get(i).getMeatFishEggs();
	}

	double fruitDist = getDistance(fruits, 2.0);
	double dairyDist = getDistance(dairy, 1.0);
	double starchesDist = getDistance(starches, 2.0);
	double vegetablesDist = getDistance(vegetables, 3.0);
	double meatFishEggsDist = getDistance(meatFishEggs, 2.0);

	int menuPartsMatches = getMenuPartsMatches(phenotype);

	Objectives objectives = new Objectives();
	// minimizing the differences to the ideal values
	objectives.add("fruits-delta", Objective.Sign.MIN, fruitDist);
	objectives.add("dairy-delta", Objective.Sign.MIN, dairyDist);
	objectives.add("starches-delta", Objective.Sign.MIN, starchesDist);
	objectives.add("vegetables-delta", Objective.Sign.MIN, vegetablesDist);
	objectives.add("meatFishEggs-delta", Objective.Sign.MIN, meatFishEggsDist);
	// maximizing the matches
	objectives.add("menu-parts-matches", Objective.Sign.MAX, menuPartsMatches);

	return objectives;
  }

  private double getDistance(Double value, Double ideal) {
	return Math.abs(value - ideal);
  }

  private int getMenuPartsMatches(Menu menu) {
	// check how many menu parts (MAIN, STARTER, DESSERT) are available in this menu
	// for a LUNCH we would like to have a STARTER, a MAIN, and a DESSERT
	// for a DINNER we would like to have a STARTER, a MAIN, and a DESSERT

	int numberOfStarters = 0;
	int numberOfMains = 0;
	int numberOfDesserts = 0;

	for (Substance substance : menu) {
	  Substance.Type type = substance.getType();
	  if(type != null){
		switch (type) {
		  case STARTER:
			numberOfStarters++;
			break;
		  case MAIN:
			numberOfMains++;
			break;
		  case DESSERT:
			numberOfDesserts++;
			break;
		}
	  }
	}
	int matches = Math.min(2, numberOfStarters) + Math.min(2, numberOfMains) + Math.min(2, numberOfDesserts);
	return matches;
  }
}