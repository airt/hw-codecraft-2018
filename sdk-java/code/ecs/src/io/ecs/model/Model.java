package io.ecs.model;

import io.ecs.common.Matrix;

public interface Model {

  /**
   * n: number of training examples
   * <p>
   * m: number of features
   *
   * @param xs :: (n × m)
   * @param ys :: (1 × m)
   */
  void fit(Matrix xs, Matrix ys);

  /**
   * @param xs :: (m × n)
   * @return ys :: (m × 1)
   */
  Matrix predict(Matrix xs);

  /**
   * RMSE
   * @param xs :: (n x m)
   * @param ys :: (1 x m)
   */
  double score(Matrix xs, Matrix ys);

  default String inspect() {
    throw new UnsupportedOperationException();
  }

}
