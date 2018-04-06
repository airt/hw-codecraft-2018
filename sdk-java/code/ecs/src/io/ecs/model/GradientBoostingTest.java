package io.ecs.model;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import org.junit.jupiter.api.Test;

import static io.ecs.common.Shortcuts.println;

class GradientBoostingTest {

    @Test
    void fit() {

        Matrix xs = Matrix.of(new double[][]{
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1},
        });

        Matrix ys = ColVector.of(
            10,
            12,
            20,
            22
        );

        Matrix xt = RowVector.of(
            0.5, 0.5
        );

        Model model = new GradientBoosting(
            5,
            i -> new LinearRegression(0.01 * Math.pow(0.1, i), 200)
        );

        model.fit(xs, ys);

        Matrix r = model.predict(xt);

        println(model.inspect());
        println(r.show());

    }

}
