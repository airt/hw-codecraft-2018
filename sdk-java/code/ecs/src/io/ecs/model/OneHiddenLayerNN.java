package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.HashMap;

/**
 * 只含有一个隐藏层的神经网络.
 */
public class OneHiddenLayerNN implements Model{
    @Override
    public void fit(Matrix xs, Matrix ys) {

    }

    @Override
    public Matrix predict(Matrix xs) {
        return null;
    }

    private static HashMap<String, Matrix> initializeParameters(int nx, int nh, int ny) {
        Matrix W1 = Numpy.randomRandn(nh, nx).mul(0.01);
        Matrix b1 = Matrix.zeros(nh, 1);
        Matrix W2 = Numpy.randomRandn(ny, nh);
        Matrix b2 = Matrix.zeros(ny, 1);

        HashMap<String, Matrix> parameters = new HashMap<>();
        parameters.put("W1", W1);
        parameters.put("b1", b1);
        parameters.put("W2", W2);
        parameters.put("b2", b2);

        return parameters;
    }

    private HashMap<String, Matrix> forwardPropagation(Matrix X, HashMap<String, Matrix> parameters) {
        Matrix W1 = parameters.get("W1");
        Matrix b1 = parameters.get("b1");
        Matrix W2 = parameters.get("W2");
        Matrix b2 = parameters.get("b2");

        Matrix Z1 = W1.mul(X).add(b1);
        Matrix A1 = NnUtils.relu(Z1);
        Matrix Z2 = W2.mul(A1).add(b2);

        HashMap<String, Matrix> cache = new HashMap<>();
        cache.put("Z1", Z1);
        cache.put("A1", A1);
        cache.put("Z2", Z2);

        return cache;
    }

    private double computeCost(Matrix A2, Matrix Y) {
        int m = Y.shape()._2();

        double cost = Numpy.square(A2.sub(Y)).sum() / (2.0*m);

        return cost;
    }

    private void backwardPropagation(HashMap<String, Matrix> parameters, HashMap<String, Matrix> cache, Matrix X, Matrix Y) {

    }
}
