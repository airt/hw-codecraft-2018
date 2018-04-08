package io.ecs.common;

import io.ecs.common.function.IntIntDoubleDoubleToDoubleFunction;
import io.ecs.common.function.IntIntDoubleToDoubleFunction;
import io.ecs.common.function.IntIntToDoubleFunction;
import io.ecs.common.matrix.impl.*;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

import static io.ecs.common.Shortcuts.throwing;

public interface Matrix {

    static Matrix of(double[]... values) {
        return new NaiveMatrix(values);
    }

    static Matrix of(int nRows, int nCols, DoubleSupplier f) {
        return of(nRows, nCols, (i, j) -> f.getAsDouble());
    }

    static Matrix of(int nRows, int nCols, IntIntToDoubleFunction f) {
        return zeros(nRows, nCols).map((i, j, x) -> f.apply(i, j));
    }

    static Matrix zeros(int nRows, int nCols) {
        return new ConstantMatrix(nRows, nCols);
    }

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
     * transpose
     */
    default Matrix t() {
        return new TransposedMatrix(this);
    }

    /**
     * @return matrix <p> (1 × nCols) if axis = 0 <p> (nRows × 1) if axis = 1
     */
    default Matrix min(int axis) {
        return NaiveMatrixOps.min(this, axis);
    }

    /**
     * @return matrix <p> (1 × nCols) if axis = 0 <p> (nRows × 1) if axis = 1
     */
    default Matrix max(int axis) {
        return NaiveMatrixOps.max(this, axis);
    }

    /**
     * @return matrix <p> (1 × nCols) if axis = 0 <p> (nRows × 1) if axis = 1
     */
    default Matrix mean(int axis) {
        return NaiveMatrixOps.mean(this, axis);
    }

    /**
     * @return matrix <p> (1 × nCols) if axis = 0 <p> (nRows × 1) if axis = 1
     */
    default Matrix std(int axis) {
        return std(axis, mean(axis));
    }

    /**
     * @return matrix <p> (1 × nCols) if axis = 0 <p> (nRows × 1) if axis = 1
     */
    default Matrix std(int axis, Matrix means) {
        return NaiveMatrixOps.std(this, axis, means);
    }

    default double sum() {
        return NaiveMatrixOps.sum(this);
    }

    /**
     * @return matrix <p> (nRows × 1)
     */
    default ColVector rowSum() {
        return NaiveMatrixOps.rowSum(this);
    }

    /**
     * @return matrix <p> (1 × nCols)
     */
    default RowVector colSum() {
        return NaiveMatrixOps.colSum(this);
    }

    default Matrix concatenate(Matrix m, int axis) {
        return (
            axis == 0 ? concatenateV(m) :
                axis == 1 ? concatenateH(m) :
                    throwing(new IllegalArgumentException("axis = " + axis))
        );
    }

    default Matrix concatenateV(Matrix m) {
        return new VerticalConcatenatedMatrix(this, m);
    }

    default Matrix concatenateH(Matrix m) {
        return new HorizontalConcatenatedMatrix(this, m);
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

    default int fixRow(int row) {
        return row < 0 ? row + rows() : row;
    }

    default int fixCol(int col) {
        return col < 0 ? col + cols() : col;
    }

    /**
     * m[i, j]
     * <p>
     * m[-1, -1] == m[nRows - 1, nCols - 1]
     */
    double get(int row, int col);

    default Matrix get(int rowA, int colA, int rowZ, int colZ) {
        return new RefMatrix(this, fixRow(rowA), fixCol(colA), fixRow(rowZ), fixCol(colZ));
    }

    /**
     * m[i, :]
     */
    RowVector row(int row);

    /**
     * m[i:j, :]
     */
    default Matrix rows(int rowA, int rowZ) {
        return get(rowA, 0, rowZ, cols() - 1);
    }

    /**
     * m[:, j]
     */
    ColVector col(int col);

    /**
     * m[:, i:j]
     */
    default Matrix cols(int colA, int colZ) {
        return get(0, colA, rows() - 1, colZ);
    }

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
