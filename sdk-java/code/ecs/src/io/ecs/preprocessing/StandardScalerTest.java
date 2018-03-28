package io.ecs.preprocessing;

import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardScalerTest {

  @Test
  void scale() {
    Matrix featuresUnscaled = Matrix.of(new double[][]{
      {1, -1, 2},
      {2, 0, 0},
      {0, 1, -1},
    });

    StandardScaler scaler = new StandardScaler();
    scaler.fit(featuresUnscaled);
    Matrix features = scaler.transform(featuresUnscaled);

    Matrix mas = features.meanAndStdOfRows();
    RowVector means = mas.row(0);
    RowVector stds = mas.row(1);

    for (double mean : means) {
      assertEquals(0, mean, 1e-9);
    }

    for (double std : stds) {
      assertEquals(1, std, 1e-9);
    }

    // println("features-unscaled:");
    // println(featuresUnscaled.show());
    // println("features:");
    // println(features.show());
  }

}
