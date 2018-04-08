package io.ecs.preprocessing;

import io.ecs.common.Matrix;
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

        Matrix means = features.mean(1);
        Matrix stds = features.std(1, means);

        for (double mean : means.col(0)) {
            assertEquals(0, mean, 1e-9);
        }

        for (double std : stds.col(0)) {
            assertEquals(1, std, 1e-9);
        }

        // println("features-unscaled:");
        // println(featuresUnscaled.show());
        // println("features:");
        // println(features.show());
    }

}
