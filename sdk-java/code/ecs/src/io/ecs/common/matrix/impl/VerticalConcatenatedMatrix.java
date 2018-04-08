package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;

public class VerticalConcatenatedMatrix implements Matrix {

    private Matrix m1;
    private Matrix m2;

    public VerticalConcatenatedMatrix(Matrix m1, Matrix m2) {
        if (m1.cols() != m2.cols()) throw new IllegalArgumentException();
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public double get(int row, int col) {
        row = fixRow(row);
        col = fixCol(col);
        return row < m1.rows() ? m1.get(row, col) : m2.get(row - m1.rows(), col);
    }

    @Override
    public RowVector row(int row) {
        row = fixRow(row);
        return row < m1.rows() ? m1.row(row) : m2.row(row - m1.rows());
    }

    @Override
    public ColVector col(int col) {
        col = fixCol(col);
        return m1.col(col).concatenateV(m2.col(col)).col(0);
    }

    @Override
    public int rows() {
        return m1.rows() + m2.rows();
    }

    @Override
    public int cols() {
        return m1.cols();
    }

}
