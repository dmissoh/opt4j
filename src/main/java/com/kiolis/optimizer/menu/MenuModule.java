package com.kiolis.optimizer.menu;

import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Constant;

public class MenuModule extends ProblemModule {

  @Constant(value = "size")
  protected int size = 100;

  public int getSize() {
	return size;
  }

  public void setSize(int size) {
	this.size = size;
  }

  protected void config() {
	bindProblem(MenuCreator.class, MenuDecoder.class, MenuEvaluator.class);
  }
}