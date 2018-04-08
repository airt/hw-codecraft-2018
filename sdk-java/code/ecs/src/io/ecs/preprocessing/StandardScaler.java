package io.ecs.preprocessing;

import io.ecs.common.Matrix;

public class StandardScaler {

    private Matrix mu;
    private Matrix sigma;

    public void fit(Matrix x) {
        mu = x.mean(1); // mean(x)
        sigma = x.std(1, mu); // std(x)
    }

    public Matrix transform(Matrix x) {
        return x.sub(mu).dotDiv(sigma);
    }

}
