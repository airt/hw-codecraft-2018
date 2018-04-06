package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.iterator.ConstantIterator;

import java.util.Iterator;

public class ConstantMatrix implements Matrix, RowVector, ColVector {

    private final int nRows;
    private final int nCols;
    private final double value;

    public ConstantMatrix(int nRows, int nCols) {
        this(nRows, nCols, 0);
    }

    public ConstantMatrix(int nRows, int nCols, double value) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.value = value;
    }

    @Override
    public Matrix t() {
        return new ConstantMatrix(cols(), rows(), value);
    }

    @Override
    public double get(int row, int col) {
        return value;
    }

    @Override
    public RowVector row(int row) {
        return new ConstantMatrix(1, cols(), value);
    }

    @Override
    public ColVector col(int col) {
        return new ConstantMatrix(rows(), 1, value);
    }

    @Override
    public int rows() {
        return nRows;
    }

    @Override
    public int cols() {
        return nCols;
    }

    @Override
    public Iterator<Double> iterator() {
        // as row vector
        if (rows() == 1) return ConstantIterator.of(value, cols());
        // as col vector
        if (cols() == 1) return ConstantIterator.of(value, rows());
        // not vector
        throw new IllegalStateException();
    }

}
