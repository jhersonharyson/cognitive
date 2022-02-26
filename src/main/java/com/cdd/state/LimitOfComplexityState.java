package com.cdd.state;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class LimitOfComplexityState {
    int limitOfComplexity = 0;
    int currentComplexity = 0;
}
