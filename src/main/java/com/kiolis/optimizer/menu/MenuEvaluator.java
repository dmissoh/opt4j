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

	for (int i = 0; i < phenotype.size(); i++) {
	  fruits += phenotype.get(i).getFruits();
	  dairy += phenotype.get(i).getDairy();
	  starches += phenotype.get(i).getStarches();
	}

	//fruits: 360g per day
	//dairy: 250g per day
	//starches: 400g per day

	double fruitDist = getDistance(fruits, 360.0);
	double dairyDist = getDistance(dairy, 250.0);
	double starchesDist = getDistance(starches, 400.0);

	int menuPartsMatches = getMenuPartsMatches(phenotype);

	Objectives objectives = new Objectives();
	// minimizing the differences to the ideal values
	objectives.add("fruits-delta", Objective.Sign.MIN, fruitDist);
	objectives.add("dairy-delta", Objective.Sign.MIN, dairyDist);
	objectives.add("starches-delta", Objective.Sign.MIN, starchesDist);

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
	int matches = Math.min(2, numberOfStarters) + Math.min(2, numberOfMains) + Math.min(2, numberOfDesserts);
	return matches;
  }
}