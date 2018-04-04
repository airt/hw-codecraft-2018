package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;

/**
 * 神经网络相关的一些函数
 */
public class NnUtils {

    public static Matrix relu(Matrix Z) {
        Matrix A = Numpy.maximum(0.0, Z);
        return A;
    }
}
