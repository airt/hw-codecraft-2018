package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.RowVector;
import io.ecs.common.iterator.ArrayIterator;

import java.util.Iterator;

public class NaiveColVector implements ColVector {

    private final double[] payload;

    public NaiveColVector(double[] payload) {
        this.payload = payload;
    }

    @Override
    public double get(int row, int col) {
        if (fixCol(col) != 0) throw new IndexOutOfBoundsException();
        return payload[fixRow(row)];
    }

    @Override
    public RowVector t() {
        return RowVector.of(payload);
    }

    @Override
    public RowVector row(int row) {
        return RowVector.of(payload[fixRow(row)]);
    }

    @Override
    public ColVector col(int col) {
        if (fixCol(col) != 0) throw new IndexOutOfBoundsException();
        return this;
    }

    @Override
    public int rows() {
        return payload.length;
    }

    @Override
    public int cols() {
        return 1;
    }

    @Override
    public Iterator<Double> iterator() {
        return ArrayIterator.of(payload);
    }

}
