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

    public static Matrix reluBackward(Matrix dA, Matrix Z) {
        double[][] m = new double[Z.rows()][Z.cols()];
        for (int i = 0; i < Z.rows(); i++) {
            for (int j = 0; j < Z.cols(); j++) {
                m[i][j] = Z.get(i, j) <= 0 ? 0 : dA.get(i, j);
            }
        }
        return Matrix.of(m);
    }
}
