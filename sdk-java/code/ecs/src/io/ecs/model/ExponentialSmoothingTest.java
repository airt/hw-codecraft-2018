package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ExponentialSmoothing Class Test
 */
public class ExponentialSmoothingTest {

    @Test
    void predict() {
        ExponentialSmoothing es = new ExponentialSmoothing(0.9);
        Matrix xs = Matrix.of(new double[][]{
                {29, 36, 40, 48, 54, 62, 70, 76, 85, 94, 103},
        });
        Matrix ys = Matrix.of(new double[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        });
        es.fit(xs, ys);
        Matrix m = es.predict(xs);
        System.out.println(m.show());
    }

    @Test
    void diff(){
        Matrix xs = Matrix.of(new double[][] {
            {1, 3, 6, 4, 19},
        });
        ExponentialSmoothing es = new ExponentialSmoothing();
        Matrix m = es.diff(xs);
        assertEquals(Tuple2.of(1, 4), m.shape());
        assertEquals(0.0, m.get(0, 2));
        assertEquals(20.0, m.sum());
    }

    @Test
    void smooth1() {
        ExponentialSmoothing es = new ExponentialSmoothing(0.4);
        Matrix diff = Matrix.of(new double[][] {
                {1, 4, 4, 9, 12},
        });
        Matrix m = es.smooth1(diff);
        assertEquals(Tuple2.of(1, 5), m.shape());
        assertEquals("2.2", String.format("%.1f", m.get(0, 0)));
        System.out.println(m.show());
    }

    @Test
    void smooth2() {
        ExponentialSmoothing es = new ExponentialSmoothing(0.5);
        Matrix diff = Matrix.of(new double[][] {
                {1, 4, 4, 9, 12},
        });
        Matrix m = es.smooth1(diff);
        assertEquals(Tuple2.of(1, 5), m.shape());
        assertEquals("2.0", String.format("%.1f", m.get(0, 0)));
        System.out.println(m.show());
    }

    @Test
    void calca() {
        ExponentialSmoothing es = new ExponentialSmoothing();
        Matrix s1 = Matrix.of(new double[][] {
                {5, 6, 3},
        });
        Matrix s2 = Matrix.of(new double[][] {
                {4, 3, 2},
        });
        Matrix m = es.calca(s1, s2);
        assertEquals(Tuple2.of(1, 3), m.shape());
        assertEquals(9.0, m.get(0,1));
        assertEquals(19.0, m.sum());
    }

    @Test
    void calcb() {
        ExponentialSmoothing es = new ExponentialSmoothing(0.2);
        Matrix s1 = Matrix.of(new double[][] {
                {5, 6, 3},
        });
        Matrix s2 = Matrix.of(new double[][] {
                {4, 3, 2},
        });
        Matrix m = es.calcb(s1, s2);
        assertEquals(Tuple2.of(1, 3), m.shape());
        assertEquals(0.75, m.get(0,1));
        assertEquals(1.25, m.sum());
    }
}
