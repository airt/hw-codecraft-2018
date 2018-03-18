package io.ecs.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixTest {

  private Matrix m;

  @BeforeEach
  void setUp() {
    double[][] data = {
      {1, 2, 3},
      {4, 5, 6},
    };
    m = Matrix.of(data);
  }

  @Test
  void get() {
    assertEquals(6, m.get(1, 2));
  }

  @Test
  void t() {
    Matrix mt = m.t();
    assertEquals(Tuple2.of(3, 2), mt.shape());
    assertEquals(6, mt.get(2, 1));
  }

  @Test
  void mul() {
    double[][] data2 = {
      {7, 10},
      {8, 11},
      {9, 12},
    };
    Matrix m2 = Matrix.of(data2);
    Matrix mm = m.mul(m2);
    assertEquals(Tuple2.of(2, 2), mm.shape());
    assertEquals(50, mm.get(0, 0));
    assertEquals(122, mm.get(1, 0));
    assertEquals(167, mm.get(1, 1));
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
