package io.ecs.model;

import io.ecs.common.Matrix;

/**
 * 二次指数平滑法
 */
public class ExponentialSmoothing implements Model{

    private double alpha;      //[0, 1)
    private Matrix a;
    private Matrix b;

    public ExponentialSmoothing(double alpha) {
        this.alpha = alpha;
    }

    public ExponentialSmoothing() {
    }

    @Override
    public void fit(Matrix xs, Matrix ys) {
        Matrix diff = diff(xs);
        Matrix s1 = smooth1(xs);
        Matrix s2 = smooth2(s1);
        a = calca(s1, s2);
        b = calcb(s1, s2);
    }

    @Override
    public Matrix predict(Matrix xs) {
        return a.col(-1).add(b.col(-1));
    }

    @Override
    public double score(Matrix xs, Matrix ys) {
        return 0;
    }

    public Matrix calcb(Matrix s1, Matrix s2) {
        if (alpha == 1) {
            throw new IllegalArgumentException();
        }
        return s1.sub(s2).mul(alpha / (1 - alpha));
    }

    public Matrix calca(Matrix s1, Matrix s2) {
        return s1.mul(2.0).sub(s2);
    }

    /**
     * 计算二次指数平滑
     * @param s1
     * @return
     */
    private Matrix smooth2(Matrix s1) {
        Matrix s0 = s1.cols(0, 2).mean(1);
        double[][] np = new double[s1.rows()][s1.cols()];
        for (int i = 0; i < s1.rows(); i++) {
            for (int j = 0; j < s1.cols(); j++) {
                np[i][j] = alpha * s1.get(i, j) + (1 - alpha) * (j == 0 ? s0.get(i, 0) : np[i][j - 1]);
            }
        }
        return Matrix.of(np);
    }

    /**
     * 计算一次指数平滑
     * @param diff
     * @return
     */
    public Matrix smooth1(Matrix diff) {
        Matrix s0 = diff.cols(0, 2).mean(1);
        double[][] np = new double[diff.rows()][diff.cols()];
        for (int i = 0; i < diff.rows(); i++) {
            for (int j = 0; j < diff.cols(); j++) {
                np[i][j] = alpha * diff.get(i, j) + (1 - alpha) * (j == 0 ? s0.get(i, 0) : np[i][j - 1]);
            }
        }
        return Matrix.of(np);
    }

    // 计算增量
    public Matrix diff(Matrix xs) {
        Matrix m1 = xs.cols(0, -2);
        Matrix m2 = xs.cols(1, -1);
        return Numpy.maximum(0.0, m2.sub(m1));
    }
}
