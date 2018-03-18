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
    double[][] np = new double[cols()][rows()];
    for (int i = 0; i < cols(); i++) {
      for (int j = 0; j < rows(); j++) {
        np[i][j] = payload[j][i];
      }
    }
    return new NaiveMatrix(np);
  }

  @Override
  public Matrix mul(Matrix rhs) {
    if (cols() != rhs.rows()) {
      throw new IllegalArgumentException();
    }
    double[][] np = new double[rows()][rhs.cols()];
    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < rhs.cols(); j++) {
        int s = 0;
        for (int k = 0; k < cols(); k++) {
          s += get(i, k) * rhs.get(k, j);
        }
        np[i][j] = s;
      }
    }
    return new NaiveMatrix(np);
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
