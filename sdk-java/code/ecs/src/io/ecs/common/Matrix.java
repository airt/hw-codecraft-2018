package io.ecs.common;

import io.ecs.common.function.IntIntDoubleDoubleToDoubleFunction;
import io.ecs.common.function.IntIntDoubleToDoubleFunction;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

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

    default Matrix add(double n) {
        return map(x -> x + n);
    }

    default Matrix add(Matrix m) {
        return zip(m, (x, y) -> x + y);
    }

    default Matrix sub(double n) {
        return map(x -> x - n);
    }

    default Matrix sub(Matrix m) {
        return zip(m, (x, y) -> x - y);
    }

    default Matrix mul(double n) {
        return map(x -> x * n);
    }

    default Matrix mul(Matrix m) {
        return NaiveMatrixOps.mul(this, m);
    }

    default Matrix dotMul(Matrix m) {
        return zip(m, (x, y) -> x * y);
    }

    default Matrix dotDiv(double n) {
        return map(x -> x / n);
    }

    default Matrix dotDiv(Matrix m) {
        return zip(m, (x, y) -> x / y);
    }

    /**
     * @return matrix m<sub>r</sub> :: (1 × nCols)
     */
    Matrix meanOfRows();

    /**
     * @return matrix m<sub>r</sub> :: (2 × nCols)
     */
    Matrix meanAndStdOfRows();

    /**
     * @return matrix m<sub>r</sub> :: (2 × nCols)
     */
    Matrix maxMinOfRows();

    default double sum() {
        return NaiveMatrixOps.sum(this);
    }

    /**
     * @return matrix m<sub>r</sub> :: (nRows × 1)
     */
    default Matrix rowSum() {
        return NaiveMatrixOps.rowSum(this);
    }

    /**
     * @return matrix m<sub>r</sub> :: (1 × nCols)
     */
    default Matrix colSum() {
        return NaiveMatrixOps.colSum(this);
    }

    default Matrix map(DoubleUnaryOperator f) {
        return map((i, j, x) -> f.applyAsDouble(x));
    }

    default Matrix map(IntIntDoubleToDoubleFunction f) {
        return NaiveMatrixOps.map(this, f);
    }

    default Matrix zip(Matrix m, DoubleBinaryOperator f) {
        return zip(m, (i, j, x, y) -> f.applyAsDouble(x, y));
    }

    default Matrix zip(Matrix m, IntIntDoubleDoubleToDoubleFunction f) {
        return NaiveMatrixOps.zip(this, m, f);
    }

    /**
     * m[i, j]
     * <p>
     * m[-1, -1] == m[nRows - 1, nCols - 1]
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
     * (nRows, nCols)
     */
    default Tuple2<Integer, Integer> shape() {
        return Tuple2.of(rows(), cols());
    }

    default String show() {
        return NaiveMatrixOps.show(this);
    }

}
