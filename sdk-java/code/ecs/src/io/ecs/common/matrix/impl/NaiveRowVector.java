package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.iterator.ArrayIterator;

import java.util.Iterator;

public class NaiveRowVector implements RowVector {

    private final double[] payload;

    public NaiveRowVector(double[] payload) {
        this.payload = payload;
    }

    @Override
    public double get(int row, int col) {
        if (row < 0) row += rows();
        if (col < 0) col += cols();
        if (row != 0) throw new IndexOutOfBoundsException();
        return payload[col];
    }

    @Override
    public ColVector t() {
        return ColVector.of(payload);
    }

    @Override
    public Matrix mean(int axis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix meanAndStdOfRows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RowVector row(int row) {
        if (row < 0) row += rows();
        if (row != 0) throw new IndexOutOfBoundsException();
        return this;
    }

    @Override
    public ColVector col(int col) {
        if (col < 0) col += cols();
        return ColVector.of(payload[col]);
    }

    @Override
    public int rows() {
        return 1;
    }

    @Override
    public int cols() {
        return payload.length;
    }

    @Override
    public Iterator<Double> iterator() {
        return ArrayIterator.of(payload);
    }

}
