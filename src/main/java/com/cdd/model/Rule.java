package com.cdd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rule {
    private String name;
    private Integer cost;
    private Integer times;
    private Object object;
}
