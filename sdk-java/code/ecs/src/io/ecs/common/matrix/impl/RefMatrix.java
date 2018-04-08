package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;

public class RefMatrix implements Matrix {

    private Matrix origin;

    private int rowA;
    private int colA;
    private int rowZ;
    private int colZ;

    public RefMatrix(Matrix origin, int rowA, int colA, int rowZ, int colZ) {
        validate(origin, rowA, colA, rowZ, colZ);
        this.origin = origin;
        this.rowA = rowA;
        this.colA = colA;
        this.rowZ = rowZ;
        this.colZ = colZ;
    }

    private void validate(Matrix origin, int rowA, int colA, int rowZ, int colZ) {
        boolean b = rowA <= rowZ && colA <= colZ;
        b &= 0 <= rowA && rowA < origin.rows();
        b &= 0 <= colA && colA < origin.cols();
        b &= 0 <= rowZ && rowZ < origin.rows();
        b &= 0 <= colZ && colZ < origin.cols();
        if (!b) throw new IllegalArgumentException();
    }

    @Override
    public double get(int row, int col) {
        return origin.get(fixRow(row) + rowA, fixCol(col) + colA);
    }

    @Override
    public RowVector row(int row) {
        return origin.row(fixRow(row) + rowA);
    }

    @Override
    public ColVector col(int col) {
        return origin.col(fixCol(col) + colA);
    }

    @Override
    public int rows() {
        return rowZ - rowA + 1;
    }

    @Override
    public int cols() {
        return colZ - colA + 1;
    }

}
