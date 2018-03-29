package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Boost implements Model {

  private List<Model> bases;

  public Boost(int n, IntFunction<Model> factory) {
    bases = IntStream.range(0, n).
      mapToObj(factory).
      collect(Collectors.toList());
  }

  @Override
  public void fit(Matrix xs, Matrix ys) {
    Matrix dys = ys;
    for (Model model : bases) {
      model.fit(xs, dys);
      dys = dys.sub(model.predict(xs));
    }
  }

  @Override
  public Matrix predict(Matrix xs) {
    return bases.stream().
      map(model -> model.predict(xs)).
      reduce(Matrix::add).
      orElseThrow(IllegalStateException::new);
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
