package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.TODO;

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
        if (row < 0) row += rows();
        if (col < 0) col += cols();
        return row < m1.rows() ? m1.get(row, col) : m2.get(row - m1.rows(), col);
    }

    @Override
    public RowVector row(int row) {
        return TODO.throwing();
    }

    @Override
    public ColVector col(int col) {
        return TODO.throwing();
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
