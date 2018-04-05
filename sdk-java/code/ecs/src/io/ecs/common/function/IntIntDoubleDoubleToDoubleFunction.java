package io.ecs.common.function;

@FunctionalInterface
public interface IntIntDoubleDoubleToDoubleFunction {

    double apply(int i, int j, double x, double y);

}
