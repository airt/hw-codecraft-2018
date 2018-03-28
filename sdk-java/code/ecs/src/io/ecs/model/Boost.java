package io.ecs.model;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Boost implements Model {

  private List<Model> bases;

  public Boost(Supplier<Model> factory) {
    bases = Arrays.asList(
      factory.get(),
      factory.get(),
      factory.get()
    );
  }

  @Override
  public void fit(Matrix xs, Matrix ys) {

    Model base0 = bases.get(0);
    base0.fit(xs, ys);
    Matrix dys0 = ys.sub(predictBatch(base0, xs));

    Model base1 = bases.get(1);
    base1.fit(xs, dys0);
    Matrix dys1 = dys0.sub(predictBatch(base1, xs));

    Model base2 = bases.get(2);
    base2.fit(xs, dys1);
    // Matrix dys2 = dys1.sub(predictBatch(base2, xs));
  }

  /**
   * TODO: move this method to `model`?
   */
  private Matrix predictBatch(Model model, Matrix xs) {
    double[] np = new double[xs.rows()];
    for (int i = 0; i < xs.rows(); i++) {
      Matrix x = xs.row(i).t();
      np[i] = model.predict(x);
    }
    return ColVector.of(np);
  }

  @Override
  public double predict(Matrix x) {
    return (
      bases.get(0).predict(x) +
        bases.get(1).predict(x) +
        bases.get(2).predict(x)
    );
  }

}
