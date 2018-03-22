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
  void get() {
    assertEquals(6, m.get(1, 2));
    assertEquals(6, m.get(-1, -1));
  }

  @Test
  void t() {
    Matrix mt = m.t();
    assertEquals(Tuple2.of(3, 2), mt.shape());
    assertEquals(6, mt.get(2, 1));
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
  void meanOfRows() {
    Matrix mor = m.meanOfRows();
    assertEquals(Tuple2.of(1, 3), mor.shape());
    assertEquals(2.5, mor.get(0, 0));
    assertEquals(3.5, mor.get(0, 1));
    assertEquals(4.5, mor.get(0, 2));
  }

  @Test
  void meanAndStdOfRows() {
    Matrix masor = m.meanAndStdOfRows();
    assertEquals(Tuple2.of(2, 3), masor.shape());
    assertEquals(2.5, masor.get(0, 0));
    assertEquals(3.5, masor.get(0, 1));
    assertEquals(4.5, masor.get(0, 2));
    assertEquals(2.1213, masor.get(1, 0), 0.001);
    assertEquals(2.1213, masor.get(1, 1), 0.001);
    assertEquals(2.1213, masor.get(1, 2), 0.001);
  }

  @Test
  void row() {
    Matrix mr = m.row(-1);
    assertEquals(Tuple2.of(1, 3), mr.shape());
    assertEquals(6, mr.get(0, 2));
  }

  @Test
  void col() {
    Matrix mc = m.col(-1);
    assertEquals(Tuple2.of(2, 1), mc.shape());
    assertEquals(6, mc.get(1, 0));
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
