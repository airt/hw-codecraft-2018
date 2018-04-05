package io.ecs.common;

import io.ecs.common.matrix.impl.NaiveColVector;

public interface ColVector extends Matrix, Iterable<Double> {

    static ColVector of(double... values) {
        return new NaiveColVector(values);
    }

    static ColVector zeros(int n) {
        return of(new double[n]);
    }

}
