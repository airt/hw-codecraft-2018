package io.ecs.model;

import io.ecs.common.Matrix;

public class Functions {

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
}
