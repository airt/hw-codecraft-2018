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
    void generateData() {
        Matrix nums = Matrix.of(new double[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3}
        });
        Matrix cpus = Matrix.of(new double[][] {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 4, 6, 2, 4, 6, 2, 4, 6, 2, 4, 6, 2, 4, 6, 2, 4, 6, 2, 4, 6}
        });
        Matrix mems = Matrix.of(new double[][] {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {4, 8, 12, 4, 8, 12, 4, 8, 12, 4, 8, 12, 4, 8, 12, 4, 8, 12, 4, 8, 12}
        });
        SlideFeatures sf = new SlideFeatures();
        String flavorName = "flavor5";
        int T = 7;
        int lookBack = 5;
        Tuple2<Tuple2<Matrix, Matrix>, Matrix> data = sf.generateData(nums, cpus, mems, flavorName, T, lookBack);
        Matrix X = data._1()._1();
        Matrix predictX = data._1()._2();
        Matrix Y = data._2();
        assertEquals(Tuple2.of(6, 4), X.shape());
        assertEquals(Tuple2.of(1, 4), Y.shape());
        assertEquals(15.0, Y.get(0, 3));
        System.out.println(X.show());
        System.out.println(Y.show());
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
