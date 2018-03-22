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
   * m<sub>1</sub> · m<sub>2</sub>
   */
  default Matrix mul(Matrix rhs) {
    if (cols() != rhs.rows()) throw new IllegalArgumentException();
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

  /**
   * @return matrix m<sub>r</sub> :: (1 × cols)
   */
  Matrix meanOfRows();

  /**
   * @return matrix m<sub>r</sub> :: (2 × cols)
   */
  Matrix maxMinOfRows();

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
    if (row < 0) row += rows();
    if (col < 0) col += cols();
    return payload[row][col];
  }

  @Override
  public Matrix t() {
    double[][] np = new double[cols()][rows()];
    for (int j = 0; j < cols(); j++) {
      for (int i = 0; i < rows(); i++) {
        np[j][i] = get(i, j);
      }
    }
    return new NaiveMatrix(np);
  }

  @Override
  public Matrix meanOfRows() {
    double[] means = new double[cols()];
    for (int j = 0; j < cols(); j++) {
      double sum = 0;
      for (int i = 0; i < rows(); i++) {
        sum += get(i, j);
      }
      double mean = sum / rows();
      means[j] = mean;
    }
    return new RowVector(means);
  }

  @Override
  public Matrix maxMinOfRows() {
    return TODO.throwing();
  }

  @Override
  public Matrix row(int row) {
    if (row < 0) row += rows();
    return new RowVector(Arrays.copyOf(payload[row], cols()));
  }

  @Override
  public Matrix col(int col) {
    if (col < 0) col += cols();
    double[] np = new double[rows()];
    for (int i = 0; i < rows(); i++) {
      np[i] = payload[i][col];
    }
    return new ColVector(np);
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

class ColVector implements Matrix {

  private final double[] payload;

  ColVector(double[] payload) {
    this.payload = payload;
  }

  @Override
  public double get(int row, int col) {
    if (row < 0) row += rows();
    if (col < 0) col += cols();
    if (col != 0) throw new IndexOutOfBoundsException();
    return payload[row];
  }

  @Override
  public Matrix t() {
    return new RowVector(Arrays.copyOf(payload, payload.length));
  }

  @Override
  public Matrix meanOfRows() {
    double sum = 0;
    for (double v : payload) {
      sum += v;
    }
    double mean = sum / rows();
    return new RowVector(new double[]{mean});
  }

  @Override
  public Matrix maxMinOfRows() {
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;
    for (double v : payload) {
      if (max < v) max = v;
      if (v < min) min = v;
    }
    return new NaiveMatrix(new double[][]{{max}, {min}});
  }

  @Override
  public Matrix row(int row) {
    if (row < 0) row += rows();
    return new RowVector(new double[]{payload[row]});
  }

  @Override
  public Matrix col(int col) {
    if (col < 0) col += cols();
    if (col != 0) throw new IndexOutOfBoundsException();
    return new ColVector(Arrays.copyOf(payload, payload.length));
  }

  @Override
  public int rows() {
    return payload.length;
  }

  @Override
  public int cols() {
    return 1;
  }

}

class RowVector implements Matrix {

  private final double[] payload;

  RowVector(double[] payload) {
    this.payload = payload;
  }

  @Override
  public double get(int row, int col) {
    if (row < 0) row += rows();
    if (col < 0) col += cols();
    if (row != 0) throw new IndexOutOfBoundsException();
    return payload[col];
  }

  @Override
  public Matrix t() {
    return new ColVector(Arrays.copyOf(payload, payload.length));
  }

  @Override
  public Matrix meanOfRows() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Matrix maxMinOfRows() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Matrix row(int row) {
    if (row < 0) row += rows();
    if (row != 0) throw new IndexOutOfBoundsException();
    return new RowVector(Arrays.copyOf(payload, payload.length));
  }

  @Override
  public Matrix col(int col) {
    if (col < 0) col += cols();
    return new ColVector(new double[]{payload[col]});
  }

  @Override
  public int rows() {
    return 1;
  }

  @Override
  public int cols() {
    return payload.length;
  }

}
