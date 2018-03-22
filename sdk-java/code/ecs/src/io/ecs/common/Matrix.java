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
   * m<sub>1</sub> - m<sub>2</sub>
   */
  default Matrix sub(Matrix m) {
    if (rows() != m.rows()) throw new IllegalArgumentException();
    double[][] np = new double[rows()][cols()];
    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < cols(); j++) {
        np[i][j] = get(i, j) - m.get(m.cols() == 1 ? 0 : i, j);
      }
    }
    return new NaiveMatrix(np);
  }

  /**
   * m<sub>1</sub> · m<sub>2</sub>
   */
  default Matrix mul(Matrix m) {
    if (cols() != m.rows()) throw new IllegalArgumentException();
    double[][] np = new double[rows()][m.cols()];
    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < m.cols(); j++) {
        int s = 0;
        for (int k = 0; k < cols(); k++) s += get(i, k) * m.get(k, j);
        np[i][j] = s;
      }
    }
    return new NaiveMatrix(np);
  }

  /**
   * m<sub>1</sub> ./ m<sub>2</sub>
   */
  default Matrix dotDiv(Matrix m) {
    if (rows() != m.rows()) throw new IllegalArgumentException();
    double[][] np = new double[rows()][cols()];
    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < cols(); j++) {
        np[i][j] = get(i, j) / m.get(m.cols() == 1 ? 0 : i, j);
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
  Matrix meanAndStdOfRows();

  /**
   * @return matrix m<sub>r</sub> :: (2 × cols)
   */
  Matrix maxMinOfRows();

  /**
   * m[i, :]
   */
  RowVector row(int row);

  /**
   * m[:, j]
   */
  ColVector col(int col);

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
      for (int i = 0; i < rows(); i++) sum += get(i, j);
      double mean = sum / rows();
      means[j] = mean;
    }
    return new RowVector(means);
  }

  @Override
  public Matrix meanAndStdOfRows() {
    double[][] mass = new double[2][cols()];
    for (int j = 0; j < cols(); j++) {
      double sum = 0;
      for (int i = 0; i < rows(); i++) sum += get(i, j);
      double mean = sum / rows();
      mass[0][j] = mean;
    }
    for (int j = 0; j < cols(); j++) {
      double mean = mass[0][j];
      double sqrsum = 0;
      for (int i = 0; i < rows(); i++) sqrsum += Math.pow(get(i, j) - mean, 2);
      double std = Math.sqrt(sqrsum / (rows() - 1));
      mass[1][j] = std;
    }
    return new NaiveMatrix(mass);
  }

  @Override
  public Matrix maxMinOfRows() {
    return TODO.throwing();
  }

  @Override
  public RowVector row(int row) {
    if (row < 0) row += rows();
    return new RowVector(Arrays.copyOf(payload[row], cols()));
  }

  @Override
  public ColVector col(int col) {
    if (col < 0) col += cols();
    double[] np = new double[rows()];
    for (int i = 0; i < rows(); i++) np[i] = payload[i][col];
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
