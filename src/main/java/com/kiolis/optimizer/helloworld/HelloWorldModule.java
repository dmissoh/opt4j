package com.kiolis.optimizer.helloworld;

import org.opt4j.core.problem.ProblemModule;

public class HelloWorldModule extends ProblemModule {
  protected void config() {
    bindProblem(HelloWorldCreator.class, HelloWorldDecoder.class, HelloWorldEvaluator.class);
  }
}