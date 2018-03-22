package io.ecs.preprocessing;

import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardScalerTest {

  @Test
  void scale() {
    StandardScaler scaler = new StandardScaler();
    Matrix featuresUnscaled = Matrix.of(new double[][]{
      {1., -1., 2.},
      {2., 0., 0.},
      {0., 1., -1.},
    });
    scaler.fit(featuresUnscaled);
    Matrix features = scaler.transform(featuresUnscaled);
    Matrix mas = features.meanAndStdOfRows();
    RowVector means = mas.row(0);
    RowVector stds = mas.row(1);
    for (int j = 0; j < means.cols(); j++) {
      assertEquals(0, means.get(0, j), 1e-9);
    }
    for (int j = 0; j < stds.cols(); j++) {
      assertEquals(1, stds.get(0, j), 1e-9);
    }
    // println("features-unscaled:");
    // println(featuresUnscaled.show());
    // println("features:");
    // println(features.show());
  }

}
