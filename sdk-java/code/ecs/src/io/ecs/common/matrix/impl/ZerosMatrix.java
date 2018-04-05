package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.iterator.ConstantIterator;

import java.util.Iterator;

public class ZerosMatrix implements Matrix, RowVector, ColVector {

    private final int nRows;
    private final int nCols;

    public ZerosMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
    }

    @Override
    public Matrix t() {
        return new ZerosMatrix(cols(), rows());
    }

    @Override
    public double get(int row, int col) {
        return 0;
    }

    @Override
    public RowVector row(int row) {
        return new ZerosMatrix(1, cols());
    }

    @Override
    public ColVector col(int col) {
        return new ZerosMatrix(rows(), 1);
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
        if (rows() == 1) return ConstantIterator.of(0, cols());
        // as col vector
        if (cols() == 1) return ConstantIterator.of(0, rows());
        // not vector
        throw new IllegalStateException();
    }

}
