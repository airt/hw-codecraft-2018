package io.ecs.common.iterator;

import java.util.Iterator;

public class ConstantIterator implements Iterator<Double> {

    private int i = 0;
    private int length;
    private double value;

    public static ConstantIterator of(double value, int length) {
        return new ConstantIterator(value, length);
    }

    private ConstantIterator(double value, int length) {
        this.length = length;
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return i < length;
    }

    @Override
    public Double next() {
        i += 1;
        return value;
    }

}
