package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.HashMap;

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

    public static HashMap<String, Matrix> linearBackward(Matrix dZ, HashMap<String, Matrix> cache) {
        Matrix A_prev = cache.get("A_prev");
        Matrix W = cache.get("W");
        double m = A_prev.shape()._2();
        Matrix dW = dZ.mul(A_prev.t()).dotDiv(m);
        Matrix db = dZ.rowSum().dotDiv(m);
        Matrix dA_prev = W.t().mul(dZ);

        HashMap<String, Matrix> re = new HashMap<>();
        re.put("dA", dA_prev);
        re.put("dW", dW);
        re.put("db", db);

        return re;
    }
}
