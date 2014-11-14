package com.kiolis.optimizer.menu.models;

import com.google.inject.Inject;
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

    private int size;

    protected List<Substance> substances = new ArrayList<Substance>();

    protected List<Substance> starters = new ArrayList<Substance>();
    protected List<Substance> mains = new ArrayList<Substance>();
    protected List<Substance> desserts = new ArrayList<Substance>();

    @Inject
    public MenuProblem(@Constant(value = "size") int size) {
        createNewRandomSubstances(size);
        starters = getSubstancesForType(Substance.Type.STARTER);
        mains = getSubstancesForType(Substance.Type.MAIN);
        desserts = getSubstancesForType(Substance.Type.DESSERT);
        this.size = size;
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

    public int getIndex(Substance substance){
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

    private List<Substance> createNewRandomSubstances(int size) {
        for (int i = 0; i < size; i++) {
            // fruits -> 360g per day
            // dairy  -> 250g per day
            // starches  -> 400g per day
            final double fruits = generateRandom(0, 500) / 6;
            final double dairy = generateRandom(0, 400) / 6;
            final double starches = generateRandom(0, 900) / 6;

            final Substance substance = new Substance(i, fruits, dairy, starches, getRandomType());
            System.out.println("substance: " + substance);
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
