package io.ecs.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MatrixTest {

    private final int mr = 6;
    private final int mc = 9;

    private final Matrix m = Matrix.of(mr, mc, (i, j) -> (i + 1) * 10 + (j + 1));

    @Test
    void zeros() {
        Matrix m0 = Matrix.zeros(5, 10);
        assertEquals(Tuple2.of(5, 10), m0.shape());
        assertEquals(Tuple2.of(10, 5), m0.t().shape());
        assertEquals(0, m0.get(0, 0));
        assertEquals(0, m0.get(4, 9));
        assertEquals(0, m0.get(-1, -1));
    }

    @Test
    void mul() {
        Matrix m1 = Matrix.of(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix m2 = Matrix.of(new double[][]{
            {7, 10},
            {8, 11},
            {9, 12},
        });
        Matrix mm = m1.mul(m2);
        assertEquals(Tuple2.of(2, 2), mm.shape());
        assertEquals(50, mm.get(0, 0));
        assertEquals(68, mm.get(0, 1));
        assertEquals(122, mm.get(1, 0));
        assertEquals(167, mm.get(1, 1));
    }

    @Test
    void t() {
        Matrix mm = m.t();
        assertEquals(Tuple2.of(mc, mr), mm.shape());
        assertEquals(12, mm.get(1, 0));
        assertSame(m, m.t().t());
    }

    @Test
    void min() {
        Matrix m1 = m.min(0);
        assertEquals(Tuple2.of(1, mc), m1.shape());
        assertEquals(12, m1.get(0, 1));
        Matrix m2 = m.min(1);
        assertEquals(Tuple2.of(mr, 1), m2.shape());
        assertEquals(21, m2.get(1, 0));
    }

    @Test
    void max() {
        Matrix m1 = m.max(0);
        assertEquals(Tuple2.of(1, mc), m1.shape());
        assertEquals(62, m1.get(0, 1));
        Matrix m2 = m.max(1);
        assertEquals(Tuple2.of(mr, 1), m2.shape());
        assertEquals(29, m2.get(1, 0));
    }

    @Test
    void mean() {
        Matrix m1 = m.mean(0);
        assertEquals(Tuple2.of(1, mc), m1.shape());
        assertEquals(36, m1.get(0, 0));
        assertEquals(37, m1.get(0, 1));
        assertEquals(38, m1.get(0, 2));
        Matrix m2 = m.mean(1);
        assertEquals(Tuple2.of(mr, 1), m2.shape());
        assertEquals(15, m2.get(0, 0));
        assertEquals(25, m2.get(1, 0));
        assertEquals(35, m2.get(2, 0));
    }

    @Test
    void std() {
        System.out.println(m.show());
        Matrix m1 = m.std(0);
        assertEquals(Tuple2.of(1, mc), m1.shape());
        assertEquals(18.70829, m1.get(0, 0), 0.001);
        assertEquals(18.70829, m1.get(0, 1), 0.001);
        assertEquals(18.70829, m1.get(0, 2), 0.001);
        Matrix m2 = m.std(1);
        assertEquals(Tuple2.of(mr, 1), m2.shape());
        assertEquals(2.73861, m2.get(0, 0), 0.001);
        assertEquals(2.73861, m2.get(1, 0), 0.001);
        assertEquals(2.73861, m2.get(2, 0), 0.001);
    }

    @Test
    void map() {
        Matrix mm = m.map((i, j, x) -> (i + 1) * 1000 + (j + 1) * 100 + x);
        assertEquals(Tuple2.of(mr, mc), mm.shape());
        assertEquals(1111, mm.get(0, 0));
        assertEquals(1212, mm.get(0, 1));
        assertEquals(1313, mm.get(0, 2));
        assertEquals(2121, mm.get(1, 0));
        assertEquals(2222, mm.get(1, 1));
        assertEquals(2323, mm.get(1, 2));
    }

    @Test
    void zip() {
        Matrix mm = m.zip(m, (i, j, x, y) -> (i + 1) * 100000 + (j + 1) * 10000 + x * 100 + y);
        assertEquals(Tuple2.of(mr, mc), mm.shape());
        assertEquals(111111, mm.get(0, 0));
        assertEquals(121212, mm.get(0, 1));
        assertEquals(131313, mm.get(0, 2));
        assertEquals(212121, mm.get(1, 0));
        assertEquals(222222, mm.get(1, 1));
        assertEquals(232323, mm.get(1, 2));
    }

    @Test
    void zipRow() {
        Matrix mm = m.zip(m.row(0), (i, j, x, y) -> (i + 1) * 100000 + (j + 1) * 10000 + x * 100 + y);
        assertEquals(Tuple2.of(mr, mc), mm.shape());
        assertEquals(111111, mm.get(0, 0));
        assertEquals(121212, mm.get(0, 1));
        assertEquals(131313, mm.get(0, 2));
        assertEquals(212111, mm.get(1, 0));
        assertEquals(222212, mm.get(1, 1));
        assertEquals(232313, mm.get(1, 2));
    }

    @Test
    void zipCol() {
        Matrix mm = m.zip(m.col(0), (i, j, x, y) -> (i + 1) * 100000 + (j + 1) * 10000 + x * 100 + y);
        assertEquals(Tuple2.of(mr, mc), mm.shape());
        assertEquals(111111, mm.get(0, 0));
        assertEquals(121211, mm.get(0, 1));
        assertEquals(131311, mm.get(0, 2));
        assertEquals(212121, mm.get(1, 0));
        assertEquals(222221, mm.get(1, 1));
        assertEquals(232321, mm.get(1, 2));
    }

    @Test
    void get() {
        assertEquals(11, m.get(0, 0));
        assertEquals(57, m.get(-2, -3));
        assertEquals(23, m.get(1, 2));
        assertEquals(69, m.get(-1, -1));
    }

    @Test
    void getByRange() {
        Matrix m1 = m.get(0, 0, -1, -1);
        assertEquals(Tuple2.of(mr, mc), m1.shape());
        assertEquals(11, m1.get(0, 0));
        assertEquals(57, m1.get(-2, -3));
        assertEquals(23, m1.get(1, 2));
        assertEquals(69, m1.get(-1, -1));
        Matrix mr1 = m1.row(0);
        assertEquals(Tuple2.of(1, mc), mr1.shape());
        assertEquals(11, mr1.get(0, 0));
        Matrix mc1 = m1.col(0);
        assertEquals(Tuple2.of(mr, 1), mc1.shape());
        assertEquals(11, mc1.get(0, 0));
        Matrix m2 = m.get(1, 1, -2, -2);
        assertEquals(Tuple2.of(mr - 2, mc - 2), m2.shape());
        assertEquals(22, m2.get(0, 0));
        assertEquals(46, m2.get(-2, -3));
        assertEquals(34, m2.get(1, 2));
        assertEquals(58, m2.get(-1, -1));
    }

    @Test
    void row() {
        Matrix mm = m.row(-1);
        assertEquals(Tuple2.of(1, mc), mm.shape());
        assertEquals(62, mm.get(0, 1));
    }

    @Test
    void col() {
        Matrix mm = m.col(-1);
        assertEquals(Tuple2.of(mr, 1), mm.shape());
        assertEquals(29, mm.get(1, 0));
    }

    @Test
    void rowsByRange() {
        Matrix mm = m.rows(-1, -1);
        assertEquals(Tuple2.of(1, mc), mm.shape());
        assertEquals(62, mm.get(0, 1));
    }

    @Test
    void colsByRange() {
        Matrix mm = m.cols(-1, -1);
        assertEquals(Tuple2.of(mr, 1), mm.shape());
        assertEquals(29, mm.get(1, 0));
    }

    @Test
    void rows() {
        assertEquals(mr, m.rows());
    }

    @Test
    void cols() {
        assertEquals(mc, m.cols());
    }

    @Test
    void shape() {
        assertEquals(Tuple2.of(mr, mc), m.shape());
    }

}
