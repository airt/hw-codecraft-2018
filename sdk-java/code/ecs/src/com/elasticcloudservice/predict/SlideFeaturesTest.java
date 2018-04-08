package com.elasticcloudservice.predict;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.Tuple2;
import io.ecs.common.matrix.impl.NaiveRowVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * SlideFeatures Test
 */
public class SlideFeaturesTest {

    RowVector rowVector = null;

    @BeforeEach
    void setUp() {
        double[] orig = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        rowVector = new NaiveRowVector(orig);
    }

    @Test
    void generateSlidesByTimes() {
        SlideFeatures sf = new SlideFeatures();
        NaiveRowVector r = sf.generateSlidesByTimes(rowVector, 7);
        assertEquals(15, r.cols());
        assertEquals(28.0, r.get(0, 0));
        assertEquals(126.0, r.get(0, 14));
        System.out.println(r.show());
    }

    @Test
    void generateFeatures() {
        SlideFeatures sf = new SlideFeatures();
        Matrix r = sf.generateFeatures(rowVector, 5);
        assertEquals(Tuple2.of(17, 5), r.shape());
        assertEquals(21.0, r.get(16, 4));
        assertEquals(7.0, r.get(2, 4));
        System.out.println(r.show());
    }

    @Test
    void generateY() {
        SlideFeatures sf = new SlideFeatures();
        RowVector r = sf.generateY(rowVector, 5, 7);
        assertEquals(Tuple2.of(1, 10), r.shape());
        assertEquals(12.0, r.get(0, 0));
        assertEquals(21.0, r.get(0, 9));
        System.out.println(r.show());
    }
}
