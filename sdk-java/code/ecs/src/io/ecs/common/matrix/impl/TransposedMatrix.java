package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.Vector;

import java.util.Iterator;

public class TransposedMatrix implements Matrix, RowVector, ColVector {

    private Matrix origin;

    public TransposedMatrix(Matrix origin) {
        this.origin = origin;
    }

    @Override
    public Matrix t() {
        return origin;
    }

    @Override
    public double get(int row, int col) {
        return origin.get(col, row);
    }

    @Override
    public RowVector row(int row) {
        return (RowVector) origin.col(row).t();
    }

    @Override
    public ColVector col(int col) {
        return (ColVector) origin.row(col).t();
    }

    @Override
    public int rows() {
        return origin.cols();
    }

    @Override
    public int cols() {
        return origin.rows();
    }

    @Override
    public Iterator<Double> iterator() {
        if (origin instanceof Vector) return ((Vector) origin).iterator();
        throw new IllegalStateException();
    }

}
