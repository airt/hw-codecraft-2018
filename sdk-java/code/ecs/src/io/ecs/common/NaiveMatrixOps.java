package io.ecs.common;

class NaiveMatrixOps {

  static Matrix sub(Matrix lhs, Matrix rhs) {
    if (lhs.cols() != rhs.cols()) throw new IllegalArgumentException();
    double[][] np = new double[lhs.rows()][lhs.cols()];
    for (int i = 0; i < lhs.rows(); i++) {
      for (int j = 0; j < lhs.cols(); j++) {
        np[i][j] = lhs.get(i, j) - rhs.get(rhs.rows() == 1 ? 0 : i, j);
      }
    }
    return new NaiveMatrix(np);
  }

  static Matrix mul(Matrix lhs, Matrix rhs) {
    if (lhs.cols() != rhs.rows()) throw new IllegalArgumentException();
    double[][] np = new double[lhs.rows()][rhs.cols()];
    for (int i = 0; i < lhs.rows(); i++) {
      for (int j = 0; j < rhs.cols(); j++) {
        int s = 0;
        for (int k = 0; k < lhs.cols(); k++) s += lhs.get(i, k) * rhs.get(k, j);
        np[i][j] = s;
      }
    }
    return new NaiveMatrix(np);
  }

  static Matrix dotDiv(Matrix lhs, Matrix rhs) {
    if (lhs.cols() != rhs.cols()) throw new IllegalArgumentException();
    double[][] np = new double[lhs.rows()][lhs.cols()];
    for (int i = 0; i < lhs.rows(); i++) {
      for (int j = 0; j < lhs.cols(); j++) {
        np[i][j] = lhs.get(i, j) / rhs.get(rhs.rows() == 1 ? 0 : i, j);
      }
    }
    return new NaiveMatrix(np);
  }

  static String show(Matrix m) {
    StringBuilder lines = new StringBuilder();
    for (int i = 0; i < m.rows(); i++) {
      for (int j = 0; j < m.cols(); j++) {
        lines.append(String.format("% 10.4f", m.get(i, j)));
      }
      lines.append('\n');
    }
    return lines.toString();
  }

}
