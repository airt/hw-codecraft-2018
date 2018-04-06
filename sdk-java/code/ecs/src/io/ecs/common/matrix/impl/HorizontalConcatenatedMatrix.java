package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.TODO;

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
        if (row < 0) row += rows();
        if (col < 0) col += cols();
        return col < m1.cols() ? m1.get(row, col) : m2.get(row, col - m1.cols());
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
        return m1.rows();
    }

    @Override
    public int cols() {
        return m1.cols() + m2.cols();
    }

}
