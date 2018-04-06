package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;

public class NaiveMatrix implements Matrix {

    private final double[][] payload;

    public NaiveMatrix(double[][] payload) {
        this.payload = payload;
    }

    @Override
    public double get(int row, int col) {
        if (row < 0) row += rows();
        if (col < 0) col += cols();
        return payload[row][col];
    }

    @Override
    public RowVector row(int row) {
        if (row < 0) row += rows();
        return RowVector.of(payload[row]);
    }

    @Override
    public ColVector col(int col) {
        if (col < 0) col += cols();
        double[] np = new double[rows()];
        for (int i = 0; i < rows(); i++) np[i] = payload[i][col];
        return ColVector.of(np);
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
