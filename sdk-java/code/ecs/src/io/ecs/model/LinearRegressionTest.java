package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.BeforeEach;
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

}