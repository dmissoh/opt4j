package com.kiolis.optimizer.menu;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.de.DifferentialEvolutionModule;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.mopso.MOPSOModule;
import org.opt4j.optimizers.rs.RandomSearchModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;
import org.opt4j.viewer.ViewerModule;

/**
 * @author dimitri
 * @version 0.1
 * @since 12/11/14
 * Copyright (c) 2014 Kiolis Software France
 */
public class StartMenuOptimization {
    public static void main(String[] args) {

        // The problem definition module
        MenuModule menuModule = new MenuModule();

        /**
         * The brute force optimization module
         */
        RandomSearchModule randomSearchModule = new RandomSearchModule();

        /**
         * The optimization modules below changes the order in of parts (starter, main, dessert) since mutation are done
         */
        EvolutionaryAlgorithmModule evolutionaryAlgorithmModule = new EvolutionaryAlgorithmModule();
        SimulatedAnnealingModule simulatedAnnealingModule = new SimulatedAnnealingModule();

        /**
         * The optimization modules below are only restricted to DoubleGenotype
         */
        MOPSOModule mopsoModule = new MOPSOModule();
        DifferentialEvolutionModule differentialEvolutionModule = new DifferentialEvolutionModule();// restricted only to DoubleGenotype

        // The viewer module
        ViewerModule viewer = new ViewerModule();
        viewer.setCloseOnStop(true);

        Opt4JTask task = new Opt4JTask(false);

        task.init(randomSearchModule, menuModule, viewer);
        //task.init(evolutionaryAlgorithmModule, menuModule , viewer);

        try {
            task.execute();
            Archive archive = task.getInstance(Archive.class);
            for (Individual individual : archive) {
                System.out.println("Genotype: " + individual.getGenotype().toString());
                System.out.println("Phenotype: " + individual.getPhenotype().toString());
                System.out.println("Objectives: " + individual.getObjectives().toString());
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            task.close();
        }
    }
}
