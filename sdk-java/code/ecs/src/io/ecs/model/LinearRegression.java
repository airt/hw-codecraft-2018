package io.ecs.model;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;

class LinearRegression implements Model {

  private Matrix theta;
  private double alpha;
  private int iterations;

  public LinearRegression(double alpha, int iterations) {
    this.alpha = alpha;
    this.iterations = iterations;
  }

  @Override
  public void fit(Matrix features, Matrix labels) {
    theta = ColVector.of(new double[features.cols()]);
    int m = features.rows();
    for (int i = 0; i < iterations; i++) {
      theta = theta.sub(
        features.t().
          mul(features.mul(theta).sub(labels)).
          mul(alpha / m)
      );
    }
  }

  @Override
  public Matrix predict(Matrix features) {
    return features.mul(theta);
  }

  @Override
  public String inspect() {
    return "[LinearRegression]\n" + "  θ =\n" + theta.show();
  }

}
