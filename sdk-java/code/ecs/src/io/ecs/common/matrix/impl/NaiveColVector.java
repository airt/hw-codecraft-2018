package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
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
        if (row < 0) row += rows();
        if (col < 0) col += cols();
        if (col != 0) throw new IndexOutOfBoundsException();
        return payload[row];
    }

    @Override
    public RowVector t() {
        return RowVector.of(payload);
    }

    @Override
    public Matrix mean(int axis) {
        double sum = 0;
        for (double v : payload) sum += v;
        double mean = sum / rows();
        return RowVector.of(mean);
    }

    @Override
    public Matrix meanAndStdOfRows() {
        double sum = 0;
        for (double v : payload) sum += v;
        double mean = sum / rows();
        double sqrsum = 0;
        for (double v : payload) sqrsum += Math.pow(v - mean, 2);
        double std = Math.sqrt(sqrsum / (rows() - 1));
        return new NaiveMatrix(new double[][]{{mean}, {std}});
    }

    @Override
    public RowVector row(int row) {
        if (row < 0) row += rows();
        return RowVector.of(payload[row]);
    }

    @Override
    public ColVector col(int col) {
        if (col < 0) col += cols();
        if (col != 0) throw new IndexOutOfBoundsException();
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
