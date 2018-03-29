package io.ecs.model;

import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import org.junit.jupiter.api.Test;

import static io.ecs.common.Shortcuts.println;

class LinearRegressionTest {

  @Test
  void fit() {
    Matrix xs = Matrix.of(new double[][]{
      {1, 0, 0},
      {1, 0, 1},
      {1, 1, 0},
      {1, 1, 1},
    });
    Matrix ys = Matrix.of(new double[][]{
      {10},
      {12},
      {20},
      {22},
    });
    Matrix xt = RowVector.of(1, 0.5, 0.5);

    Model model = new LinearRegression(0.001, 100000);
    model.fit(xs, ys);
    Matrix r = model.predict(xt);

    println(model.inspect());
    println(r.show());
  }

}
