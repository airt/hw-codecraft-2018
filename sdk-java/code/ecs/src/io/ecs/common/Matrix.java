package io.ecs.common;

import java.util.Arrays;

public interface Matrix {

  static Matrix of(double[][] raw) {
    return new NaiveMatrix(raw);
  }

  /**
   * m[i, j]
   * <p>
   * m[-1, -1] == m[rows - 1, cols - 1]
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

  /**
   * m[i, :]
   */
  Matrix row(int row);

  /**
   * m[:, j]
   */
  Matrix col(int col);

  /**
   * how many rows?
   */
  int rows();

  /**
   * how many columns?
   */
  int cols();

  /**
   * (rows, cols)
   */
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
    if (row < 0) {
      row += rows();
    }
    if (col < 0) {
      col += cols();
    }
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
  public Matrix row(int row) {
    if (row < 0) {
      row += rows();
    }
    double[][] np = new double[1][];
    np[0] = Arrays.copyOf(payload[row], cols());
    return new NaiveMatrix(np);
  }

  @Override
  public Matrix col(int col) {
    if (col < 0) {
      col += cols();
    }
    double[][] np = new double[rows()][1];
    for (int i = 0; i < rows(); i++) {
      np[i][0] = payload[i][col];
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
