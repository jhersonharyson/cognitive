package com.cdd.state;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class JavaClassesState {
    Set<String> listOfClasses = new HashSet<>();
    String rawListOfClasses = null;
    String javaVersion = null;
}
