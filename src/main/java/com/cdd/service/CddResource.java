package com.cdd.service;

import com.cdd.model.Rule;

import java.util.Set;

public interface CddResource {

   Set<Rule> loadMetrics();

   int limitOfComplexity();

}
