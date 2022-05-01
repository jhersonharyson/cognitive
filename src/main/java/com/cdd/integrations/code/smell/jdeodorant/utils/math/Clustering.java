package com.cdd.integrations.code.smell.jdeodorant.utils.math;

import com.cdd.integrations.code.smell.jdeodorant.core.distance.Entity;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Clustering {
    protected double[][] distanceMatrix;

    public static Clustering getInstance(double[][] distanceMatrix) {
        return new Hierarchical(distanceMatrix);
    }

    public abstract HashSet<Cluster> clustering(ArrayList<Entity> entities);
}
