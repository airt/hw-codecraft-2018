package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.Random;

public class Numpy {

    public static Matrix sigmoid(Matrix z) {
        double[][] s = new double[z.rows()][z.cols()];
        for (int i = 0; i < z.rows(); i++) {
            for (int j = 0; j < z.cols(); j++) {
                s[i][j] = 1.0 / (1.0 + Math.exp(-1 * z.get(i, j)));
            }
        }
        return Matrix.of(s);
    }

    // 初始化w
    public static Matrix initialize_w_zeros(int dim) {
        double[][] ans = new double[dim][1];
        for (int i = 0; i < ans.length; i++) {
            ans[i][0] = 0.0;
        }
        return Matrix.of(ans);
    }

    public static Matrix log(Matrix m) {
        double[][] ans = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                ans[i][j] = Math.log(m.get(i, j));
            }
        }
        return Matrix.of(ans);
    }

    public static Matrix square(Matrix m) {
        double[][] ans = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                ans[i][j] = Math.pow(m.get(i, j), 2);
            }
        }
        return Matrix.of(ans);
    }

    // 生成rows*cols的随机正态分布
    public static Matrix randomRandn(int rows, int cols) {
        double[][] m = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m[i][j] = random.nextGaussian();
            }
        }
        return Matrix.of(m);
    }

    public static Matrix maximum(double x1, Matrix x2) {
        double[][] m = new double[x2.rows()][x2.cols()];
        for (int i = 0; i < x2.rows(); i++) {
            for (int j = 0; j < x2.cols(); j++) {
                m[i][j] = x1 > x2.get(i, j) ? x2.get(i, j) : x1;
            }
        }
        return Matrix.of(m);
    }
}
