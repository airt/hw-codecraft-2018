package io.ecs.common.iterator;

import java.util.Iterator;

public class ArrayIterator implements Iterator<Double> {

    private int i = 0;
    private double[] values;

    public static ArrayIterator of(double... values) {
        return new ArrayIterator(values);
    }

    private ArrayIterator(double[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Double next() {
        return values[i++];
    }

}
