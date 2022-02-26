package com.cdd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Rule {
    private String name;
    private Integer cost;
    private Set<String> parameters;
    private Integer times;
    private Object object;
}
