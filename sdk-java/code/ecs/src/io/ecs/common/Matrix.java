package io.ecs.common;

public interface Matrix {

  static Matrix of(double[][] raw) {
    return new NaiveMatrix(raw);
  }

  /**
   * m[i, j]
   */
  double get(int row, int col);

  /**
   * transpose
   */
  Matrix t();

  /**
   * m1 * m2
   */
  Matrix mul(Matrix rhs);

  int rows();

  int cols();

  default Tuple2<Integer, Integer> shape() {
    return Tuple2.of(rows(), cols());
  }

}

class NaiveMatrix implements Matrix {

  private final double[][] payload;

  NaiveMatrix(double[][] payload) {
    this.payload = payload;
  }

  @Override
  public double get(int row, int col) {
    return payload[row][col];
  }

  @Override
  public Matrix t() {
    return TODO.throwing();
  }

  @Override
  public Matrix mul(Matrix rhs) {
    return TODO.throwing();
  }

  @Override
  public int rows() {
    return payload.length;
  }

  @Override
  public int cols() {
    return payload.length == 0 ? 0 : payload[0].length;
  }

}
