package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearRegressionTest {

    @Test
    void predict() {
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {3, 4},
        });
        Matrix Y = Matrix.of(new double[][] {
                {12, 17},
        });
        LinearRegression lr = new LinearRegression(0.001, 50000);
        lr.fit(X, Y);
        Matrix m = lr.predict(X);
        assertEquals("12.00", String.format("%.2f", m.get(0, 0)));
        assertEquals("17.00", String.format("%.2f", m.get(0, 1)));
    }

    @Test
    void propagate() {
        Matrix w = Matrix.of(new double[][] {
                {1},
                {2},
        });
        double b = 2;
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {3, 4},
        });
        Matrix Y = Matrix.of(new double[][] {
                {1, 0},
        });
        LinearRegression lr = new LinearRegression();
        Tuple2<Matrix, double []> tuple2 = lr.propagate(w, b, X, Y);
        assertEquals(-16.0, tuple2._1().get(0, 0));
        assertEquals(-36.0, tuple2._1().get(1, 0));
        assertEquals(-10.0, tuple2._2()[0]);
        assertEquals(52.0, tuple2._2()[1]);
    }

    @Test
    void optimize() {
        Matrix w = Matrix.of(new double[][] {
                {1.0},
                {2.0},
        });
        double b = 2.0;
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {3, 4},
        });
        Matrix Y = Matrix.of(new double[][] {
                {12, 17},
        });
        LinearRegression lr = new LinearRegression();
        Tuple2<Matrix, Double> t = lr.optimize(w, b, X, Y, 50000, 0.001, false);
        assertEquals("12.00", String.format("%.2f",t._1().t().mul(X).add(t._2()).get(0, 0)));
        assertEquals("17.00", String.format("%.2f",t._1().t().mul(X).add(t._2()).get(0, 1)));
    }

}