package com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.cfg;

public class GraphEdge {
    protected final GraphNode src;
    protected final GraphNode dst;

    GraphEdge(GraphNode src, GraphNode dst) {
        this.src = src;
        this.dst = dst;
    }

    public GraphNode getSrc() {
        return src;
    }

    public GraphNode getDst() {
        return dst;
    }
}
