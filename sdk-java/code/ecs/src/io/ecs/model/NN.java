package io.ecs.model;

import io.ecs.common.Matrix;
import java.util.HashMap;

/**
 * 多层神经网络
 */
class NN implements Model {

    private int numIterations;
    private double learningRate;
    HashMap<String, Matrix> parameters;
    int[] layerDims;

    public NN() {
    }

    public NN(int numIterations, double learningRate, int[] layerDims) {
        this.numIterations = numIterations;
        this.learningRate = learningRate;
        this.layerDims = layerDims;
        this.parameters = new HashMap<>();
    }

    @Override
    public void fit(Matrix X, Matrix Y) {
        boolean print_cost = true;

        parameters = initialParametersDeep(layerDims);
        int l = parameters.size() / 2;

        for (int i = 0; i < numIterations; i++) {
            HashMap<String, Matrix>[] caches = deepModelForward(X, parameters);
            Matrix AL = caches[l-1].get("A");
            double cost = computeCost(AL, Y);
            HashMap<String, Matrix> grads = deepModelBackward(AL, Y, caches);
            updateParameters(parameters, grads, learningRate);
            if (print_cost && i % 100 == 0) {
                System.out.printf("Cost after iteration %d: %f", i, cost);
                System.out.println();
            }
        }
    }

    @Override
    public Matrix predict(Matrix X) {
        return deepModelForward(X, parameters)[layerDims.length- 2].get("A");
    }

    public HashMap<String, Matrix> initialParametersDeep(int[] layerDims) {
        HashMap<String, Matrix> parameters = new HashMap<>();
        for (int i = 1; i < layerDims.length; i++) {
            parameters.put("W" + i, Numpy.randomRandn(layerDims[i], layerDims[i - 1]).mul(0.01));
            parameters.put("b" + i, Numpy.randomRandn(layerDims[i], 1).mul(0.01));
        }
        return parameters;
    }

    public HashMap<String, Matrix> linearForward(Matrix A, Matrix W, Matrix b) {
        Matrix Z = W.mul(A).add(b);
        HashMap<String, Matrix> cache = new HashMap<>();
        cache.put("Z", Z);
        cache.put("A", A);
        cache.put("W", W);
        cache.put("b", b);
        return cache;
    }

    public HashMap<String, Matrix> linearActivationForward(Matrix A_prev, Matrix W, Matrix b, String activation) {
        HashMap<String, Matrix> cache = new HashMap<>();
        Matrix Z = W.mul(A_prev).add(b);
        cache.put("Z", Z);
        if (activation.equals("relu")) {
            cache.put("A", NnUtils.relu(Z));
        } else if (activation.equals("none")) {
            cache.put("A", Z);
        } else {
            throw new IllegalArgumentException();
        }
        cache.put("A_prev", A_prev);
        cache.put("W", W);
        cache.put("b", b);
        return cache;
    }

    public  HashMap<String, Matrix>[] deepModelForward(Matrix X, HashMap<String, Matrix> parameters) {
        Matrix A = X;
        int l = parameters.size() / 2;
        HashMap<String, Matrix>[] caches = new HashMap[l];
        for (int i = 1; i < l; i++) {
            Matrix A_pre = A;
            HashMap<String, Matrix> cache = linearActivationForward(A_pre, parameters.get("W" + i), parameters.get("b" + i), "relu");
            A = cache.get("A");
            cache.remove("A");
            caches[i-1] = cache;
        }
        HashMap<String, Matrix> cache = linearActivationForward(A, parameters.get("W" + l), parameters.get("b" + l), "none");
        caches[l-1] = cache;

        return caches;
    }

    public double computeCost(Matrix AL, Matrix Y) {
        int m = Y.shape()._2();

        double cost = Numpy.square(AL.sub(Y)).sum() / (2.0 * m);

        return cost;
    }

    public HashMap<String, Matrix> linearActivationBackward(Matrix dA, HashMap<String, Matrix> cache) {
        Matrix dZ = NnUtils.reluBackward(dA, cache.get("Z"));
        return NnUtils.linearBackward(dZ, cache);
    }

    public HashMap<String, Matrix> deepModelBackward(Matrix AL, Matrix Y, HashMap<String, Matrix>[] caches) {
        int l = caches.length;
        HashMap<String, Matrix> grads = new HashMap<>();
        double m = AL.shape()._2();

        Matrix dAL = AL.sub(Y).dotDiv(m);
        HashMap<String, Matrix> gradL = NnUtils.linearBackward(dAL, caches[l-1]);
        grads.put("dA" + l, gradL.get("dA"));
        grads.put("dW" + l, gradL.get("dW"));
        grads.put("db" + l, gradL.get("db"));

        for (int i = l - 2; i > -1; i--) {
            HashMap<String, Matrix> current = caches[i];
            HashMap<String, Matrix> temp = linearActivationBackward(grads.get("dA" + (i + 2)), current);
            grads.put("dA" + (i + 1), temp.get("dA"));
            grads.put("dW" + (i + 1), temp.get("dW"));
            grads.put("db" + (i + 1), temp.get("db"));
        }
        return grads;
    }

    public HashMap<String, Matrix> updateParameters(HashMap<String, Matrix> parameters, HashMap<String, Matrix> grads, double learingRate) {
        int l = parameters.size() / 2;
        for (int i = 0; i < l; i++) {
            parameters.put("W" + (i + 1), parameters.get("W" + (i + 1)).sub(grads.get("dW" + (i + 1)).mul(learingRate)));
            parameters.put("b" + (i + 1), parameters.get("b" + (i + 1)).sub(grads.get("db" + (i + 1)).mul(learingRate)));
        }
        return parameters;
    }

}
