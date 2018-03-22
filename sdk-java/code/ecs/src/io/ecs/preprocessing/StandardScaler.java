package io.ecs.preprocessing;

import io.ecs.common.Matrix;
import io.ecs.common.RowVector;

public class StandardScaler {

  private RowVector mu;
  private RowVector sigma;

  public void fit(Matrix x) {
    Matrix mas = x.meanAndStdOfRows();
    mu = mas.row(0); // mean(x)
    sigma = mas.row(1); // std(x)
  }

  public Matrix transform(Matrix x) {
    return x.sub(mu).dotDiv(sigma);
  }

}
