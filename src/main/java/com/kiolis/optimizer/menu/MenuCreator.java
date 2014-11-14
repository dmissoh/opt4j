package com.kiolis.optimizer.menu;

import com.google.inject.Inject;
import com.kiolis.optimizer.menu.models.MenuProblem;
import com.kiolis.optimizer.menu.models.Substance;
import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Creator;

import java.util.Collections;

public class MenuCreator implements Creator<SelectGenotype<Substance>> {

    protected final MenuProblem problem;

    @Inject
    public MenuCreator(MenuProblem problem) {
        this.problem = problem;
    }

    public SelectGenotype<Substance> create() {
        SelectGenotype<Substance> genotype = new SelectGenotype<Substance>(problem.getSubstances());
        //genotype.init(random, 6);
        createTypeBasedGenotype(genotype);

        /*
        System.out.println("1 " + genotype.getValue(0));
        System.out.println("2 " + genotype.getValue(1));
        System.out.println("3 " + genotype.getValue(2));
        System.out.println("4 " + genotype.getValue(3));
        System.out.println("5 " + genotype.getValue(4));
        System.out.println("6 " + genotype.getValue(5));
        System.out.println(" ");
        */

        return genotype;
    }


    private void createTypeBasedGenotype(SelectGenotype<Substance> genotype){

        Collections.shuffle(problem.getStarters());
        Collections.shuffle(problem.getMains());
        Collections.shuffle(problem.getDesserts());

        genotype.add(problem.getIndex(problem.getStarters().get(0)));
        genotype.add(problem.getIndex(problem.getMains().get(0)));
        genotype.add(problem.getIndex(problem.getDesserts().get(0)));

        genotype.add(problem.getIndex(problem.getStarters().get(1)));
        genotype.add(problem.getIndex(problem.getMains().get(1)));
        genotype.add(problem.getIndex(problem.getDesserts().get(1)));
    }
}