package io.ecs.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixTest {

    private Matrix m;

    @BeforeEach
    void setUp() {
        m = Matrix.of(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
    }

    @Test
    void zeros() {
        Matrix m0 = Matrix.zeros(10, 10);
        assertEquals(Tuple2.of(10, 10), m0.shape());
        assertEquals(0, m0.get(0, 0));
        assertEquals(0, m0.get(9, 9));
    }

    @Test
    void mul() {
        Matrix m2 = Matrix.of(new double[][]{
            {7, 10},
            {8, 11},
            {9, 12},
        });
        Matrix mm = m.mul(m2);
        assertEquals(Tuple2.of(2, 2), mm.shape());
        assertEquals(50, mm.get(0, 0));
        assertEquals(122, mm.get(1, 0));
        assertEquals(167, mm.get(1, 1));
    }

    @Test
    void t() {
        Matrix mm = m.t();
        assertEquals(Tuple2.of(3, 2), mm.shape());
        assertEquals(6, mm.get(2, 1));
    }

    @Test
    void mean() {
        Matrix m1 = m.mean(0);
        assertEquals(Tuple2.of(1, 3), m1.shape());
        assertEquals(2.5, m1.get(0, 0));
        assertEquals(3.5, m1.get(0, 1));
        assertEquals(4.5, m1.get(0, 2));
        Matrix m2 = m.mean(1);
        assertEquals(Tuple2.of(2, 1), m2.shape());
        assertEquals(2.0, m2.get(0, 0));
        assertEquals(5.0, m2.get(1, 0));
    }

    @Test
    void min() {
        Matrix m1 = m.min(0);
        assertEquals(Tuple2.of(1, 3), m1.shape());
        assertEquals(3, m1.get(0, 2));
        Matrix m2 = m.min(1);
        assertEquals(Tuple2.of(2, 1), m2.shape());
        assertEquals(4, m2.get(1, 0));
    }

    @Test
    void max() {
        Matrix m1 = m.max(0);
        assertEquals(Tuple2.of(1, 3), m1.shape());
        assertEquals(6, m1.get(0, 2));
        Matrix m2 = m.max(1);
        assertEquals(Tuple2.of(2, 1), m2.shape());
        assertEquals(6, m2.get(1, 0));
    }

    @Test
    void meanAndStdOfRows() {
        Matrix mm = m.meanAndStdOfRows();
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(2.5, mm.get(0, 0));
        assertEquals(3.5, mm.get(0, 1));
        assertEquals(4.5, mm.get(0, 2));
        assertEquals(2.1213, mm.get(1, 0), 0.001);
        assertEquals(2.1213, mm.get(1, 1), 0.001);
        assertEquals(2.1213, mm.get(1, 2), 0.001);
    }

    @Test
    void map() {
        Matrix mm = m.map((i, j, x) -> (i + 1) * 100 + (j + 1) * 10 + x);
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(111, mm.get(0, 0));
        assertEquals(122, mm.get(0, 1));
        assertEquals(133, mm.get(0, 2));
        assertEquals(214, mm.get(1, 0));
        assertEquals(225, mm.get(1, 1));
        assertEquals(236, mm.get(1, 2));
    }

    @Test
    void zip() {
        Matrix mm = m.zip(m, (i, j, x, y) -> (i + 1) * 1000 + (j + 1) * 100 + x * 10 + y);
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(1111, mm.get(0, 0));
        assertEquals(1222, mm.get(0, 1));
        assertEquals(1333, mm.get(0, 2));
        assertEquals(2144, mm.get(1, 0));
        assertEquals(2255, mm.get(1, 1));
        assertEquals(2366, mm.get(1, 2));
    }

    @Test
    void zipRow() {
        Matrix mm = m.zip(m.row(0), (i, j, x, y) -> (i + 1) * 1000 + (j + 1) * 100 + x * 10 + y);
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(1111, mm.get(0, 0));
        assertEquals(1222, mm.get(0, 1));
        assertEquals(1333, mm.get(0, 2));
        assertEquals(2141, mm.get(1, 0));
        assertEquals(2252, mm.get(1, 1));
        assertEquals(2363, mm.get(1, 2));
    }

    @Test
    void zipCol() {
        Matrix mm = m.zip(m.col(0), (i, j, x, y) -> (i + 1) * 1000 + (j + 1) * 100 + x * 10 + y);
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(1111, mm.get(0, 0));
        assertEquals(1221, mm.get(0, 1));
        assertEquals(1331, mm.get(0, 2));
        assertEquals(2144, mm.get(1, 0));
        assertEquals(2254, mm.get(1, 1));
        assertEquals(2364, mm.get(1, 2));
    }

    @Test
    void get() {
        assertEquals(1, m.get(0, 0));
        assertEquals(1, m.get(-2, -3));
        assertEquals(6, m.get(1, 2));
        assertEquals(6, m.get(-1, -1));
    }

    @Test
    void getRange1() {
        Matrix mm = m.get(0, 0, -1, -1);
        assertEquals(Tuple2.of(2, 3), mm.shape());
        assertEquals(1, mm.get(0, 0));
        assertEquals(1, mm.get(-2, -3));
        assertEquals(6, mm.get(1, 2));
        assertEquals(6, mm.get(-1, -1));
    }

    @Test
    void getRange2() {
        Matrix mm = m.get(1, 1, -1, -1);
        assertEquals(Tuple2.of(1, 2), mm.shape());
        assertEquals(5, mm.get(0, 0));
        assertEquals(5, mm.get(-1, -2));
        assertEquals(6, mm.get(0, 1));
        assertEquals(6, mm.get(-1, -1));
    }

    @Test
    void row() {
        Matrix mm = m.row(-1);
        assertEquals(Tuple2.of(1, 3), mm.shape());
        assertEquals(6, mm.get(0, 2));
    }

    @Test
    void col() {
        Matrix mm = m.col(-1);
        assertEquals(Tuple2.of(2, 1), mm.shape());
        assertEquals(6, mm.get(1, 0));
    }

    @Test
    void rowsRange() {
        Matrix mm = m.rows(-1, -1);
        assertEquals(Tuple2.of(1, 3), mm.shape());
        assertEquals(6, mm.get(0, 2));
    }

    @Test
    void colsRange() {
        Matrix mm = m.cols(-1, -1);
        assertEquals(Tuple2.of(2, 1), mm.shape());
        assertEquals(6, mm.get(1, 0));
    }

    @Test
    void rows() {
        assertEquals(2, m.rows());
    }

    @Test
    void cols() {
        assertEquals(3, m.cols());
    }

    @Test
    void shape() {
        assertEquals(Tuple2.of(2, 3), m.shape());
    }

}
