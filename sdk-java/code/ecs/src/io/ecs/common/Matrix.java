package io.ecs.common;

public interface Matrix {

  static Matrix of(double[]... payload) {
    return new NaiveMatrix(payload);
  }

  static Matrix zeros(int i, int j) {
    return of(new double[i][j]);
  }

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

  default Matrix add(double b) {
	  return NaiveMatrixOps.add(this, b);
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

  default Matrix dotMul(Matrix m) {
	  return NaiveMatrixOps.dotMul(this, m);
  }

  /**
   * m<sub>1</sub> ./ m<sub>2</sub>
   */
  default Matrix dotDiv(Matrix m) {
    return NaiveMatrixOps.dotDiv(this, m);
  }

  default Matrix dotDiv(double b) {
	  return NaiveMatrixOps.dotDiv(this, b);
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
   * m[i, j]
   * <p>
   * m[-1, -1] == m[rows - 1, cols - 1]
   */
  double get(int row, int col);

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

  default double sum() {
	  return NaiveMatrixOps.sum(this);
  }

}
