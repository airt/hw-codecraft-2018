package io.ecs.common.matrix.impl;

import io.ecs.common.Matrix;
import io.ecs.common.RowVector;

public class NaiveMatrix implements Matrix {

    private final double[][] payload;

    public NaiveMatrix(double[][] payload) {
        this.payload = payload;
    }

    @Override
    public double get(int row, int col) {
        return payload[fixRow(row)][fixCol(col)];
    }

    @Override
    public RowVector row(int row) {
        return RowVector.of(payload[fixRow(row)]);
    }

    @Override
    public int rows() {
        return payload.length;
    }

    @Override
    public int cols() {
        return payload.length == 0 ? 0 : payload[0].length;
    }

}
