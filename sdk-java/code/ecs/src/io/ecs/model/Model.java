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
   * @param xs :: (m × n)
   * @return ys :: (m × 1)
   */
  Matrix predict(Matrix xs);

  default String inspect() {
    return "";
  }

}
