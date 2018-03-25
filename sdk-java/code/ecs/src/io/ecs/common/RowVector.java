package io.ecs.common;

import java.util.Arrays;

public class RowVector implements Matrix {

  public static Matrix of(double... raw) {
    return new RowVector(raw);
  }

  private final double[] payload;

  public RowVector(double[] payload) {
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
  public Matrix meanAndStdOfRows() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Matrix maxMinOfRows() {
    throw new UnsupportedOperationException();
  }

  @Override
  public RowVector row(int row) {
    if (row < 0) row += rows();
    if (row != 0) throw new IndexOutOfBoundsException();
    return new RowVector(Arrays.copyOf(payload, payload.length));
  }

  @Override
  public ColVector col(int col) {
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
