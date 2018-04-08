package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
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
        if (fixRow(row) != 0) throw new IndexOutOfBoundsException();
        return payload[fixCol(col)];
    }

    @Override
    public ColVector t() {
        return ColVector.of(payload);
    }

    @Override
    public RowVector row(int row) {
        if (fixRow(row) != 0) throw new IndexOutOfBoundsException();
        return this;
    }

    @Override
    public ColVector col(int col) {
        return ColVector.of(payload[fixCol(col)]);
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
