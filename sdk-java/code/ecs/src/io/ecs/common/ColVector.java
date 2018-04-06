package io.ecs.common;

import io.ecs.common.matrix.impl.ConstantMatrix;
import io.ecs.common.matrix.impl.NaiveColVector;

public interface ColVector extends Vector {

    static ColVector of(double... values) {
        return new NaiveColVector(values);
    }

    static ColVector zeros(int nRows) {
        return new ConstantMatrix(nRows, 1);
    }

    static ColVector ones(int nRows) {
        return new ConstantMatrix(nRows, 1, 1);
    }

}
