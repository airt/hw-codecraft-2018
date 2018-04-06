package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NN Test Class
 */
public class NNTest {

    @Test
    void predict() {
        NN nn = new NN(50000, 0.001, new int[] {2, 4, 1});
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {2, 3},
        });
        Matrix Y = Matrix.of(new double[][] {
                {7, 10},
        });
        nn.fit(X, Y);
        Matrix testX = Matrix.of(new double[][] {
                {1, 2, 4, 5},
                {2, 3, 5, 5},
        });
        // 2*x_1 + x_2 + 3
        Matrix testY = nn.predict(testX);
        // [7, 10, 16, 18]
        System.out.println(testY.show());
    }

    @Test
    void initialParametersDeep() {
        NN nn = new NN();
        int[] layerDims = {5, 4, 3};
        HashMap<String, Matrix> parameters = nn.initialParametersDeep(layerDims);
        System.out.println(parameters.get("W1").show());
        assertEquals(Tuple2.of(4, 5), parameters.get("W1").shape());
        assertEquals(Tuple2.of(4, 1), parameters.get("b1").shape());
        assertEquals(Tuple2.of(3, 4), parameters.get("W2").shape());
        assertEquals(Tuple2.of(3, 1), parameters.get("b2").shape());
    }

    @Test
    void linearForward() {
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
                {3, 4},
                {5, 6},
        });
        Matrix W = Matrix.of(new double[][] {
                {1, 2, 4},
        });
        Matrix b = Matrix.of(new double[][] {
                {3},
        });
        NN nn = new NN();
        Matrix Z = nn.linearForward(X, W, b).get("Z");
        assertEquals(30.0, Z.get(0, 0));
        assertEquals(37.0, Z.get(0, 1));
    }

    @Test
    void linearActivationForward() {
        Matrix A_prev = Matrix.of(new double[][] {
                {-1, 2},
                {-3, 4},
                {-5, 6},
        });
        Matrix W = Matrix.of(new double[][] {
                {1, 2, 4},
        });
        Matrix b = Matrix.of(new double[][] {
                {3},
        });
        NN nn = new NN();
        HashMap<String, Matrix> cache = nn.linearActivationForward(A_prev, W, b, "relu");
        Matrix A = cache.get("A");
        assertEquals(0.0, A.get(0, 0));
        assertEquals(37.0, A.get(0, 1));
    }

    @Test
    void deepModelForward() {
        Matrix X = Matrix.of(new double[][] {
                {-1, 2},
                {-3, 4},
                {-5, 6},
        });
        Matrix W1 = Matrix.of(new double[][] {
                {1, 2, 4},
                {2, 4, 6},
        });
        Matrix b1 = Matrix.of(new double[][] {
                {3},
                {2},
        });
        Matrix W2 = Matrix.of(new double[][] {
                {1, 2},
        });
        Matrix b2 = Matrix.of(new double[][] {
                {5},
        });
        HashMap<String, Matrix> parameters = new HashMap<>();
        parameters.put("W1", W1);
        parameters.put("b1", b1);
        parameters.put("W2", W2);
        parameters.put("b2", b2);
        NN nn = new NN();
        HashMap<String, Matrix>[] caches = nn.deepModelForward(X, parameters);
        Matrix A2 = caches[caches.length - 1].get("A");
        assertEquals(5.0, A2.get(0, 0));
        assertEquals(158.0, A2.get(0, 1));
        assertEquals(2, caches.length);
    }

    @Test
    void computeCost() {
        Matrix AL = Matrix.of(new double[][] {
                {3, 6},
        });
        Matrix Y = Matrix.of(new double[][] {
                {4, 9},
        });
        NN nn = new NN();
        assertEquals(2.5, nn.computeCost(AL, Y));
    }

    @Test
    void linearActivationBackward() {
        Matrix AL = Matrix.of(new double[][] {
                {3.1980455 ,  7.85763489},
        });
        Matrix A_prev = Matrix.of(new double[][] {
                {-1.02387576,  1.12397796},
                {-1.62328545,  0.64667545},
                {-1.74314104, -0.59664964},
        });
        Matrix W = Matrix.of(new double[][] {
                {0.74505627,  1.97611078, -1.24412333},
        });
        Matrix b = Matrix.of(new double[][] {
                {5},
        });
        Matrix Z = Matrix.of(new double[][] {
                {3.1980455 ,  7.85763489},
        });
        HashMap<String, Matrix> cache = new HashMap<>();
        cache.put("Z", Z);
        cache.put("A_prev", A_prev);
        cache.put("W", W);
        cache.put("b", b);
        NN nn = new NN();
        HashMap<String, Matrix> d = nn.linearActivationBackward(AL, cache);
        System.out.println(d.get("dW").show());
        System.out.println(d.get("db").show());
        assertEquals("5.5278", String.format("%.4f", d.get("db").get(0, 0)));
        assertEquals("-5.1314", String.format("%.4f", d.get("dW").get(0, 2)));
    }

    @Test
    void deepModelBackward() {
        Matrix AL = Matrix.of(new double[][] {
                {1.78862847, 0.43650985},
        });
        Matrix Y = Matrix.of(new double[][] {
                {1, 0},
        });
        HashMap<String, Matrix>[] caches = new HashMap[2];
        Matrix A1 = Matrix.of(new double[][] {
                {0.09649747, -1.8634927},
                {-0.2773882,  -0.35475898},
                {-0.08274148, -0.62700068},
                {-0.04381817, -0.47721803},
        });
        Matrix W1 = Matrix.of(new double[][] {
                {-1.31386475, 0.88462238, 0.88131804, 1.70957306},
                {0.05003364, -0.40467741, -0.54535995, -1.54647732},
                {0.98236743, -1.10106763, -1.18504653, -0.2056499 },
        });
        Matrix b1 = Matrix.of(new double[][] {
                {1.48614836},
                {0.23671627},
                {-1.02378514}
        });
        Matrix Z1 = Matrix.of(new double[][] {
                {-0.7129932,   0.62524497},
                {-0.16051336, -0.76883635},
                {-0.23003072,  0.74505627},
        });
        HashMap<String, Matrix> cache1 = new HashMap<>();
        cache1.put("A_prev", A1);
        cache1.put("W", W1);
        cache1.put("b", b1);
        cache1.put("Z", Z1);
        caches[0] = cache1;

        Matrix A2 = Matrix.of(new double[][] {
                { 1.97611078, -1.24412333},
                {-0.62641691, -0.80376609},
                {-2.41908317, -0.92379202},
        });
        Matrix W2 = Matrix.of(new double[][] {
                {-1.02387576,  1.12397796, -0.13191423},
        });
        Matrix b2 = Matrix.of(new double[][] {
                {-1.62328545},
        });
        Matrix Z2 = Matrix.of(new double[][] {
                {0.64667545, -0.35627076},
        });
        HashMap<String, Matrix> cache2 = new HashMap<>();
        cache2.put("A_prev", A2);
        cache2.put("W", W2);
        cache2.put("b", b2);
        cache2.put("Z", Z2);
        caches[1] = cache2;

        NN nn = new NN();
        HashMap<String, Matrix> grads = nn.deepModelBackward(AL, Y, caches);
        /**
         *
         * dW1 = [[0.20821356 0.03963827 0.07005664 0.05332098]
         *       [0.         0.         0.         0.        ]
         *       [0.02682584 0.00510692 0.00902597 0.00686978]]
         *
         * db1 = [[-0.11173296]
         *        [ 0.        ]
         *        [-0.01439547]]
         */
        System.out.println(grads.get("dW1").show());
        System.out.println(grads.get("db1").show());
    }
}
