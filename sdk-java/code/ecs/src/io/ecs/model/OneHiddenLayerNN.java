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

    public HashMap<String, Matrix> initializeParameters(int nx, int nh, int ny) {
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

    public HashMap<String, Matrix> forwardPropagation(Matrix X, HashMap<String, Matrix> parameters) {
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

    public double computeCost(Matrix A2, Matrix Y) {
        int m = Y.shape()._2();

        double cost = Numpy.square(A2.sub(Y)).sum() / (2.0*m);

        return cost;
    }

    private HashMap<String, Matrix> backwardPropagation(HashMap<String, Matrix> parameters, HashMap<String, Matrix> cache, Matrix X, Matrix Y) {
        int m = X.shape()._2();
        Matrix W1 = parameters.get("W1");
        Matrix W2 = parameters.get("W2");

        Matrix Z1 = cache.get("Z1");
        Matrix A1 = cache.get("A1");
        Matrix Z2 = cache.get("Z2");

        Matrix dW2 = A1.mul(Z2.sub(Y).t()).dotDiv(m);
        Matrix db2 = Z2.sub(Y).rowSum().dotDiv(m);
        Matrix dA1 = W1.t().mul(Z2.sub(Y).t()).dotDiv(m);
        Matrix dZ1 = NnUtils.reluBackward(dA1, Z1);
        Matrix dW1 = dZ1.mul(X.t()).dotDiv(m);
        Matrix db1 = dZ1.rowSum().dotDiv(m);

        HashMap<String, Matrix> grads = new HashMap<>();
        grads.put("dW1", dW1);
        grads.put("db1", db1);
        grads.put("dW2", dW2);
        grads.put("db2", db2);

        return grads;
    }
}
