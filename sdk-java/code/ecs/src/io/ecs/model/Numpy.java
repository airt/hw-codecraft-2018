package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.Random;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection", "unused"})
public class Numpy {

    public static Matrix sigmoid(Matrix m) {
        return m.map(Numpy::sigmoid);
    }

    public static double sigmoid(double n) {
        return 1.0 / (1.0 + Math.exp(-1 * n));
    }

    // 初始化 w
    public static Matrix initialize_w_zeros(int dim) {
        return Matrix.zeros(dim, 1);
    }

    public static Matrix log(Matrix m) {
        return m.map(Math::log);
    }

    public static Matrix square(Matrix m) {
        return m.map(x -> x * x);
    }

    // 生成 rows × cols 的随机正态分布矩阵
    public static Matrix randomRandn(int rows, int cols) {
        final Random random = new Random(1);
        return Matrix.of(rows, cols, random::nextGaussian);
    }

    public static Matrix maximum(Matrix m, double n) {
        return m.map(x -> Math.max(x, n));
    }

    public static Matrix maximum(double n, Matrix m) {
        return maximum(m, n);
    }

    public static Matrix minimum(Matrix m, double n) {
        return m.map(x -> Math.min(x, n));
    }

    public static Matrix minimum(double n, Matrix m) {
        return minimum(m, n);
    }

    public static double mean(Matrix m) {
        return m.sum() / (m.rows() * m.cols());
    }

}
