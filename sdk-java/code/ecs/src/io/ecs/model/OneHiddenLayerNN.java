package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.HashMap;

/**
 * 只含有一个隐藏层的神经网络.
 */
public class OneHiddenLayerNN implements Model {

    private int numIterations;
    private double learningRate;
    HashMap<String, Matrix> parameters;

    public OneHiddenLayerNN() {
    }

    public OneHiddenLayerNN(int numIterations, double learningRate) {
        this.numIterations = numIterations;
        this.learningRate = learningRate;
        this.parameters = new HashMap<>();
    }

    @Override
    public void fit(Matrix X, Matrix Y) {
        boolean print_cost = true;
        int nx = layerSizes(X, Y)[0];
        int nh = 4;
        int ny = layerSizes(X, Y)[2];

        parameters = initializeParameters(nx, nh, ny);
        Matrix W1 = parameters.get("W1");
        Matrix b1 = parameters.get("b1");
        Matrix W2 = parameters.get("W2");
        Matrix b2 = parameters.get("b2");

        for (int i = 0; i < numIterations; i++) {
            HashMap<String, Matrix> cache = forwardPropagation(X, parameters);
            double cost = computeCost(cache.get("Z2"), Y);
            HashMap<String, Matrix> grads = backwardPropagation(parameters, cache, X, Y);
            updateParameters(parameters, grads, learningRate);
            if (print_cost && i % 100 == 0) {
                System.out.printf("Cost after iteration %d: %f", i, cost);
                System.out.println();
            }
        }
    }

    @Override
    public Matrix predict(Matrix X) {
        return forwardPropagation(X, parameters).get("Z2");
    }

    @Override
    public double score(Matrix xs, Matrix ys) {
        throw  new UnsupportedOperationException();
    }

    public int[] layerSizes(Matrix X, Matrix Y) {
        int nx = X.shape()._1();
        int nh = 4;
        int ny = Y.shape()._1();
        return new int[] {nx, nh, ny};
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

        Matrix dZ2 = Z2.sub(Y).dotDiv(m);
        HashMap<String, Matrix> cache2 = new HashMap<>();
        cache2.put("W", W2);
        cache2.put("A_prev", A1);
        HashMap<String, Matrix> p2 = NnUtils.linearBackward(dZ2, cache2);
        Matrix dW2 = p2.get("dW");
        Matrix db2 = p2.get("db");
        Matrix dA1 = p2.get("dA");
        Matrix dZ1 = NnUtils.reluBackward(dA1, Z1);
        HashMap<String, Matrix> cache1 = new HashMap<>();
        cache1.put("W", W1);
        cache1.put("A_prev", X);
        HashMap<String, Matrix> p1 = NnUtils.linearBackward(dZ1, cache1);
        Matrix dW1 = p1.get("dW");
        Matrix db1 = p1.get("db");

        HashMap<String, Matrix> grads = new HashMap<>();
        grads.put("dW1", dW1);
        grads.put("db1", db1);
        grads.put("dW2", dW2);
        grads.put("db2", db2);

        return grads;
    }

    public HashMap<String, Matrix> updateParameters(HashMap<String, Matrix> parameters, HashMap<String, Matrix> grads, double learningRate) {
        Matrix W1 = parameters.get("W1");
        Matrix b1 = parameters.get("b1");
        Matrix W2 = parameters.get("W2");
        Matrix b2 = parameters.get("b2");

        Matrix dW1 = grads.get("dW1");
        Matrix db1 = grads.get("db1");
        Matrix dW2 = grads.get("dW2");
        Matrix db2 = grads.get("db2");

        W1 = W1.sub(dW1.mul(learningRate));
        b1 = b1.sub(db1.mul(learningRate));
        W2 = W2.sub(dW2.mul(learningRate));
        b2 = b2.sub(db2.mul(learningRate));

        parameters.put("W1", W1);
        parameters.put("b1", b1);
        parameters.put("W2", W2);
        parameters.put("b2", b2);

        return parameters;
    }
}
