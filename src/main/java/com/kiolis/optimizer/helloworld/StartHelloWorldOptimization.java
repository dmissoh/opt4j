package com.kiolis.optimizer.helloworld;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.viewer.ViewerModule;

/**
 * @author dimitri
 * @version 0.1
 * @since 12/11/14
 * Copyright (c) 2014 Kiolis Software France
 */
public class StartHelloWorldOptimization {
  public static void main(String[] args) {

	EvolutionaryAlgorithmModule evolutionaryAlgorithmModule = new EvolutionaryAlgorithmModule();
	evolutionaryAlgorithmModule.setGenerations(1000);
	evolutionaryAlgorithmModule.setAlpha(100);
	evolutionaryAlgorithmModule.setMu(25);
	evolutionaryAlgorithmModule.setLambda(25);
	evolutionaryAlgorithmModule.setCrossoverRate(0.95);

	HelloWorldModule helloWorldModule = new HelloWorldModule();

	ViewerModule viewer = new ViewerModule();
	viewer.setCloseOnStop(false);

	Opt4JTask task = new Opt4JTask(false);
	//task.init(ea, module, viewer);
	task.init(evolutionaryAlgorithmModule, helloWorldModule);
	try {
	  task.execute();
	  Archive archive = task.getInstance(Archive.class);
	  for (Individual individual : archive) {
		System.out.println("Genotype: " + individual.getGenotype().toString());
		System.out.println("Phenotype: " + individual.getPhenotype().toString());
		System.out.println("Objectives: " + individual.getObjectives().toString());
		System.out.println("----------------");
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	} finally {
	  task.close();
	}
  }
}
