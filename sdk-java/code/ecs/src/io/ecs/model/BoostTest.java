package io.ecs.model;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import org.junit.jupiter.api.Test;

import static io.ecs.common.Shortcuts.println;

class BoostTest {

  @Test
  void fit() {
    Matrix xs = Matrix.of(new double[][]{
      {0, 0},
      {1, 0},
      {1, 1},
    });
    Matrix ys = Matrix.of(new double[][]{
      {10},
      {20},
      {22},
    });
    Matrix xt = ColVector.of(0, 1);

    Model model = new Boost(() -> new LinearRegression(0.001, 10000));
    model.fit(xs, ys);
    double r = model.predict(xt);

    println(r);
    println(Math.abs(r - 12));
  }

}
