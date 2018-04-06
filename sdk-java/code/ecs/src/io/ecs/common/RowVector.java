package io.ecs.common;

import io.ecs.common.matrix.impl.ConstantMatrix;
import io.ecs.common.matrix.impl.NaiveRowVector;

public interface RowVector extends Vector {

    static RowVector of(double... values) {
        return new NaiveRowVector(values);
    }

    static RowVector zeros(int nCols) {
        return new ConstantMatrix(1, nCols);
    }

    static RowVector ones(int nCols) {
        return new ConstantMatrix(1, nCols, 1);
    }

}
