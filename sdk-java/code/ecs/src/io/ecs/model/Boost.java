package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Boost implements Model {

  List<Model> bases;

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
    Matrix dys0 = ys.sub(base0.predict(xs));

    Model base1 = bases.get(1);
    base1.fit(xs, dys0);
    Matrix dys1 = dys0.sub(base1.predict(xs));

    Model base2 = bases.get(2);
    base2.fit(xs, dys1);
  }

  @Override
  public Matrix predict(Matrix xs) {
    return (
      bases.get(0).predict(xs).
        add(bases.get(1).predict(xs)).
        add(bases.get(2).predict(xs))
    );
  }

  @Override
  public String inspect() {
    StringBuilder builder = new StringBuilder();
    builder.append("[Boost]\n");
    for (Model model : bases) {
      builder.append(model.inspect().replaceAll("\\[", "[[").replaceAll("]", "]]"));
    }
    return builder.toString();
  }

}
