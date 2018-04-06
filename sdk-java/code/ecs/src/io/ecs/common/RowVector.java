package io.ecs.common;

import io.ecs.common.matrix.impl.NaiveRowVector;

public interface RowVector extends Vector {

    static RowVector of(double... values) {
        return new NaiveRowVector(values);
    }

    static RowVector zeros(int n) {
        return of(new double[n]);
    }

}
