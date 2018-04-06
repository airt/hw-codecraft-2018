package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OneHiddenLayerNN Test Class
 */
class OneHiddenLayerNNTest {

    @Test
    void fit() {

    }

    @Test
    void predict() {
        OneHiddenLayerNN onn = new OneHiddenLayerNN(50000, 0.001);
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {2, 3},
        });
        Matrix Y = Matrix.of(new double[][] {
                {7, 10},
        });
        onn.fit(X, Y);
        Matrix testX = Matrix.of(new double[][] {
                {4},
                {5},
        });
        // 2*x_1 + x_2 + 3
        Matrix testY = onn.predict(testX);
        assertEquals("16.00", String.format("%.2f", testY.get(0, 0)));
    }

    @Test
    void initializeParameters() {
        int nx = 2;
        int nh = 4;
        int ny = 1;
        OneHiddenLayerNN onn = new OneHiddenLayerNN();
        HashMap<String, Matrix> parameters = onn.initializeParameters(nx, nh, ny);
        Matrix W1 = parameters.get("W1");
        Matrix b1 = parameters.get("b1");
        Matrix W2 = parameters.get("W2");
        Matrix b2 = parameters.get("b2");
        assertEquals(Tuple2.of(4, 2), W1.shape());
        assertEquals(Tuple2.of(4, 1), b1.shape());
        assertEquals(Tuple2.of(1, 4), W2.shape());
        assertEquals(Tuple2.of(1, 1), b2.shape());
        assertEquals("0.0", String.format("%.1f", Math.abs(W1.sum() / (nx * nh))));
        assertEquals(0.0, b2.get(0, 0));
    }

    @Test
    void forwardPropagation() {
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {2, 3},
        });
        Matrix W1 = Matrix.of(new double[][] {
                {1, 1},
                {2, 2},
                {3, 3},
                {4, 4},
        });
        Matrix b1 = Matrix.zeros(4, 1);
        Matrix W2 = Matrix.of(new double[][] {
                {1, 2, 3, 4},
        });
        Matrix b2 = Matrix.of(new double[][] {
                {1},
        });
        HashMap<String, Matrix> parameters = new HashMap<>();
        parameters.put("W1", W1);
        parameters.put("b1", b1);
        parameters.put("W2", W2);
        parameters.put("b2", b2);
        OneHiddenLayerNN oneNN = new OneHiddenLayerNN();
        HashMap<String, Matrix> cache = oneNN.forwardPropagation(X, parameters);
        Matrix Z1 = cache.get("Z1");
        Matrix A1 = cache.get("A1");
        Matrix Z2 = cache.get("Z2");
        assertEquals(10.0, Numpy.mean(Z1));
        assertEquals(10.0, Numpy.mean(A1));
        assertEquals(121.0, Numpy.mean(Z2));
    }

    @Test
    void computeCost() {
        Matrix A2 = Matrix.of(new double[][] {
                {90, 150},
        });
        Matrix Y = Matrix.of(new double[][] {
                {88, 152},
        });
        OneHiddenLayerNN oneNN = new OneHiddenLayerNN();
        assertEquals(2.0, oneNN.computeCost(A2, Y));
    }

}