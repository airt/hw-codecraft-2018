package io.ecs.model;

import io.ecs.common.Matrix;

public interface Model {

  /**
   * m: number of training examples
   * <p>
   * n: number of features
   *
   * @param xs :: (m × n)
   * @param ys :: (m × 1)
   */
  void fit(Matrix xs, Matrix ys);

  /**
   * @param x :: (n × 1)
   * @return y :: (1 × 1)
   */
  double predict(Matrix x);

}
