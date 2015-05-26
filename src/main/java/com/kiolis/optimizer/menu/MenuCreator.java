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
	SelectGenotype<Substance> genotype = new SelectGenotype<>(problem.getSubstances());
	createTypeBasedGenotype(genotype);
	return genotype;
  }


  /**
   * Create a random individual
   *
   * @param genotype
   */
  private void createTypeBasedGenotype(SelectGenotype<Substance> genotype) {

	// shuffle all starters, mains and desserts
	Collections.shuffle(problem.getStarters());
	Collections.shuffle(problem.getMains());
	Collections.shuffle(problem.getDesserts());

	// and create a new possible menu out of of them
	genotype.add(problem.getIndex(problem.getStarters().get(0)));
	genotype.add(problem.getIndex(problem.getMains().get(0)));
	genotype.add(problem.getIndex(problem.getDesserts().get(0)));

	genotype.add(problem.getIndex(problem.getStarters().get(1)));
	genotype.add(problem.getIndex(problem.getMains().get(1)));
	genotype.add(problem.getIndex(problem.getDesserts().get(1)));
  }
}