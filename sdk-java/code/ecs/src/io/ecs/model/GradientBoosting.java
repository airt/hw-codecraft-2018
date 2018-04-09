package io.ecs.model;

import io.ecs.common.Matrix;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GradientBoosting implements Model {

    private List<Model> bases;

    public GradientBoosting(int n, IntFunction<Model> creator) {
        bases = IntStream.range(0, n).
                mapToObj(creator).
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
    public double score(Matrix xs, Matrix ys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String inspect() {
        StringBuilder builder = new StringBuilder();
        builder.append("[GradientBoosting]\n");
        for (Model model : bases) {
            builder.append(model.inspect().replaceAll("\\[", "[[").replaceAll("]", "]]"));
        }
        return builder.toString();
    }

}
