package io.ecs.common;

public interface Matrix {

  static Matrix of(double[]... raw) {
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
   * m<sub>1</sub> + m<sub>2</sub>
   */
  default Matrix add(Matrix m) {
    return NaiveMatrixOps.add(this, m);
  }

  /**
   * m<sub>1</sub> - m<sub>2</sub>
   */
  default Matrix sub(Matrix m) {
    return NaiveMatrixOps.sub(this, m);
  }

  /**
   * m<sub>1</sub> · m<sub>2</sub>
   */
  default Matrix mul(Matrix m) {
    return NaiveMatrixOps.mul(this, m);
  }

  /**
   * c · m<sub>1</sub>
   */
  default Matrix mul(double c) {
    return NaiveMatrixOps.mul(this, c);
  }

  /**
   * m<sub>1</sub> ./ m<sub>2</sub>
   */
  default Matrix dotDiv(Matrix m) {
    return NaiveMatrixOps.dotDiv(this, m);
  }

  /**
   * @return matrix m<sub>r</sub> :: (1 × m.cols)
   */
  Matrix meanOfRows();

  /**
   * @return matrix m<sub>r</sub> :: (2 × m.cols)
   */
  Matrix meanAndStdOfRows();

  /**
   * @return matrix m<sub>r</sub> :: (2 × m.cols)
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

  default String show() {
    return NaiveMatrixOps.show(this);
  }

}
