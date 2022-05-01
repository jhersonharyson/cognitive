package com.cdd.integrations.code.smell.jdeodorant.utils;

@FunctionalInterface
public interface QuadriFunction<T, U, V, S, R> {
    R apply(T t, U u, V v, S s);
}
