package com.cdd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class CddMetrics {
    private Integer limitOfComplexity;
    private Set<MetricsExtends> metricsExtends;
    private Set<Rule> rules;
}
