package io.ecs.model;

import io.ecs.common.Matrix;

public interface Model {

  void fit(Matrix features, Matrix labels);

  double predict(Matrix features);

}
