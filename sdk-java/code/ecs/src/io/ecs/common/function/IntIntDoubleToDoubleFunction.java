package io.ecs.common.function;

@FunctionalInterface
public interface IntIntDoubleToDoubleFunction {

    double apply(int i, int j, double x);

}
