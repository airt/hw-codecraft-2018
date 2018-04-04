package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.TODO;
import io.ecs.common.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 线性回归模型
 */
public class LinearRegression implements Model {

    private Matrix w;

    private double b;

    private double learning_rate;

    private int num_iterations;

    public LinearRegression(double learning_rate, int num_iterations) {
        this.learning_rate = learning_rate;
        this.num_iterations = num_iterations;
    }

    @Override
    public void fit(Matrix X, Matrix Y) {
        if (X.rows() != Y.rows()) {
            try {
                throw new Exception("Features rows not equeals labels!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Matrix w = Numpy.initialize_w_zeros(X.rows());
        double b = 0.0;
        Tuple2<Matrix, Double> tuple2 = optimize(w, b, X, Y, num_iterations, learning_rate, false);
        this.w = tuple2._1();
        this.b = tuple2._2();
    }


    public Tuple2<Matrix, double []> propagate(Matrix w, double b, Matrix X, Matrix Y) {
        double m = X.shape()._2();

        Matrix A = w.t().mul(X).add(b);

        double cost = Numpy.square(A.sub(Y)).sum() / (2.0*m);

        Matrix dw = X.mul(A.sub(Y).t()).dotDiv(m);
        double db = A.sub(Y).sum() / m;
        double[] t2 = {db, cost};
        return Tuple2.of(dw, t2);
    }

    public Tuple2<Matrix, Double> optimize(Matrix w, double b, Matrix X, Matrix Y, int num_iterations, double learning_rate, boolean print_cost) {
        List<Double> costs = new ArrayList<>();
        Matrix dw = null;
        double db = 0.0;
        for (int i = 0; i < num_iterations; i++) {
            Tuple2<Matrix, double[]> tuple2 = propagate(w, b, X, Y);
            dw = tuple2._1();
            db = tuple2._2()[0];
            double cost = tuple2._2()[1];

            w = w.sub(dw.mul(learning_rate));
            b = b - learning_rate * db;

            if (i % 100 == 0) {
                costs.add(cost);
            }
            if (print_cost && i % 100 == 0) {
                System.out.printf("Cost after iteration %d: %f", i, cost);
                System.out.println();
            }
        }
        return Tuple2.of(w, b);
    }

    @Override
    public Matrix predict(Matrix X) {
        return w.t().mul(X).add(b);
    }
}
