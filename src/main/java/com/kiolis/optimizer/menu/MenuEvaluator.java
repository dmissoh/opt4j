package com.kiolis.optimizer.menu;

import com.kiolis.optimizer.menu.models.Menu;
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

        Objectives objectives = new Objectives();
        objectives.add("fruits-delta", Objective.Sign.MIN, fruitDist);
        objectives.add("dairy-delta", Objective.Sign.MIN, dairyDist);
        objectives.add("starches-delta", Objective.Sign.MIN, starchesDist);
        return objectives;
    }

    private double getDistance(Double value, Double ideal) {
        return Math.abs(value - ideal);
    }
}