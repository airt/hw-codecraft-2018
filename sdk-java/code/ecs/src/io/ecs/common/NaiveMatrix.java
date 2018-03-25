package io.ecs.common;

import java.util.Arrays;

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
    if (rows() <= 1) throw new IllegalStateException();
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
