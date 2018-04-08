package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;

public class HorizontalConcatenatedMatrix implements Matrix {

    private Matrix m1;
    private Matrix m2;

    public HorizontalConcatenatedMatrix(Matrix m1, Matrix m2) {
        if (m1.rows() != m2.rows()) throw new IllegalArgumentException();
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public double get(int row, int col) {
        row = fixRow(row);
        col = fixCol(col);
        return col < m1.cols() ? m1.get(row, col) : m2.get(row, col - m1.cols());
    }

    @Override
    public ColVector col(int col) {
        col = fixCol(col);
        return col < m1.cols() ? m1.col(col) : m2.col(col - m1.cols());
    }

    @Override
    public int rows() {
        return m1.rows();
    }

    @Override
    public int cols() {
        return m1.cols() + m2.cols();
    }

}
