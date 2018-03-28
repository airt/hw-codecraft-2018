package io.ecs.common;

import java.util.Arrays;

public class ColVector implements Matrix {

  public static ColVector of(double... raw) {
    return new ColVector(raw);
  }

  private final double[] payload;

  public ColVector(double[] payload) {
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
    for (double v : payload) sum += v;
    double mean = sum / rows();
    return new RowVector(new double[]{mean});
  }

  @Override
  public Matrix meanAndStdOfRows() {
    double sum = 0;
    for (double v : payload) sum += v;
    double mean = sum / rows();
    double sqrsum = 0;
    for (double v : payload) sqrsum += Math.pow(v - mean, 2);
    double std = Math.sqrt(sqrsum / (rows() - 1));
    return new RowVector(new double[]{mean, std});
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
  public RowVector row(int row) {
    if (row < 0) row += rows();
    return new RowVector(new double[]{payload[row]});
  }

  @Override
  public ColVector col(int col) {
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
