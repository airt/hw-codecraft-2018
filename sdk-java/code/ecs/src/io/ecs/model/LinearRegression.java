package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.TODO;

import java.util.Arrays;

/**
 * 线性回归模型
 */
class LinearRegression implements Model {

    private double[] theta;

    private double alpha;

    private int iteration;

    public LinearRegression(double alpha, int iteration) {
        this.alpha = alpha;
        this.iteration = iteration;
    }

    @Override
    public void fit(Matrix features, Matrix labels) {
        theta = new double[features.cols()];
        train(features, labels);
    }

    private void train(Matrix features, Matrix label) {
        int iteration = this.iteration;
        for (int i = 0; i < iteration; i++) {
            double[] partial_derivative = computePartialDerivative(features, label);   // 偏导数
            for (int j = 0; j < theta.length; j++) {
                theta[j] -= alpha * partial_derivative[j];
            }
        }
    }

    private double[] computePartialDerivative(Matrix features, Matrix label) {
        double[] partial_derivative = new double[theta.length];
        for (int i = 0; i < theta.length; i++) {
            partial_derivative[i] = computePartialDerivativeForTheta(i, features, label);
        }
        return partial_derivative;
    }

    private double computePartialDerivativeForTheta(int thetaI, Matrix features, Matrix label) {
        double sum = 0.0;
        for (int j = 0; j < features.rows(); j++) {
            sum += h_theta_x_i_minus_y_i_times_x_j_i(j, thetaI, features, label);
        }
        return sum / features.rows();
    }

    private double h_theta_x_i_minus_y_i_times_x_j_i(int r, int thetaI, Matrix features, Matrix label) {
        Matrix oneRow = features.row(r);
        double result = 0.0;
        for (int k = 0; k < oneRow.cols(); k++) {
            result += theta[k] * oneRow.get(0, k);
        }
        result -= label.get(r, 0);
        result *= oneRow.get(0, thetaI);
        return result;
    }

    private double computeLoss(Matrix X, Matrix Y) {
        return TODO.throwing();
    }

    @Override
    public double predict(Matrix features) {
        System.out.println(Arrays.toString(theta));
        return 0.0;
    }

    public static void main(String[] args) {
        Matrix features = Matrix.of(new double[][]{
                {1, 1},
                {1, 2},
        });
        Matrix labels = Matrix.of(new double[][]{
                {5},
                {8},
        });
        LinearRegression lr = new LinearRegression(0.001, 100000);
        lr.fit(features, labels);
        System.out.println(Arrays.toString(lr.theta));
        lr.predict(features);
    }
}
