package io.ecs.common.matrix.impl;

import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.function.IntIntDoubleDoubleToDoubleFunction;
import io.ecs.common.function.IntIntDoubleToDoubleFunction;

public class NaiveMatrixOps {

    public static Matrix mul(Matrix lhs, Matrix rhs) {
        if (lhs.cols() != rhs.rows())
            throw new IllegalArgumentException();
        double[][] np = new double[lhs.rows()][rhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < rhs.cols(); j++) {
                double s = 0;
                for (int k = 0; k < lhs.cols(); k++)
                    s += lhs.get(i, k) * rhs.get(k, j);
                np[i][j] = s;
            }
        }
        return new NaiveMatrix(np);
    }

    public static Matrix mean(Matrix m, int axis) {
        if (axis == 0) {
            return m.colSum().dotDiv(m.rows());
        }
        if (axis == 1) {
            return m.rowSum().dotDiv(m.cols());
        }
        throw new IllegalArgumentException("invalid axis " + axis);
    }

    public static Matrix std(Matrix m, int axis, Matrix means) {
        if (axis == 0) {
            if (m.rows() <= 1) throw new IllegalStateException();
            double[] np = new double[m.cols()];
            for (int j = 0; j < m.cols(); j++) {
                double mean = means.get(0, j);
                double sqrsum = 0;
                for (int i = 0; i < m.rows(); i++) sqrsum += Math.pow(m.get(i, j) - mean, 2);
                double std = Math.sqrt(sqrsum / (m.rows() - 1));
                np[j] = std;
            }
            return RowVector.of(np);
        }
        if (axis == 1) {
            if (m.cols() <= 1) throw new IllegalStateException();
            double[] np = new double[m.rows()];
            for (int i = 0; i < m.rows(); i++) {
                double mean = means.get(i, 0);
                double sqrsum = 0;
                for (int j = 0; j < m.cols(); j++) sqrsum += Math.pow(m.get(i, j) - mean, 2);
                double std = Math.sqrt(sqrsum / (m.cols() - 1));
                np[i] = std;
            }
            return ColVector.of(np);
        }
        throw new IllegalArgumentException("invalid axis " + axis);
    }

    public static double sum(Matrix m) {
        double r = 0.0;
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                r += m.get(i, j);
            }
        }
        return r;
    }

    public static ColVector rowSum(Matrix m) {
        double[] np = new double[m.rows()];
        for (int i = 0; i < m.rows(); i++) {
            double t = 0.0;
            for (int j = 0; j < m.cols(); j++) {
                t += m.get(i, j);
            }
            np[i] = t;
        }
        return new NaiveColVector(np);
    }

    public static RowVector colSum(Matrix m) {
        double[] np = new double[m.cols()];
        for (int i = 0; i < m.cols(); i++) {
            double t = 0.0;
            for (int j = 0; j < m.rows(); j++) {
                t += m.get(j, i);
            }
            np[i] = t;
        }
        return new NaiveRowVector(np);
    }

    public static RowVector row(Matrix m, int row) {
        row = m.fixRow(row);
        double[] np = new double[m.cols()];
        for (int j = 0; j < m.cols(); j++) np[j] = m.get(row, j);
        return RowVector.of(np);
    }

    public static ColVector col(Matrix m, int col) {
        col = m.fixCol(col);
        double[] np = new double[m.rows()];
        for (int i = 0; i < m.rows(); i++) np[i] = m.get(i, col);
        return ColVector.of(np);
    }

    public static Matrix map(Matrix m, IntIntDoubleToDoubleFunction f) {
        double[][] np = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i += 1) {
            for (int j = 0; j < m.cols(); j++) {
                np[i][j] = f.apply(i, j, m.get(i, j));
            }
        }
        return Matrix.of(np);
    }

    public static Matrix zip(Matrix m1, Matrix m2, IntIntDoubleDoubleToDoubleFunction f) {
        if (!canZip(m1, m2))
            throw new IllegalArgumentException(String.format(
                "M[%d × %d] ∘ M[%d × %d]",
                m1.rows(), m1.cols(),
                m2.rows(), m2.cols()
            ));
        double[][] np = new double[m1.rows()][m1.cols()];
        for (int i = 0; i < m1.rows(); i += 1) {
            for (int j = 0; j < m1.cols(); j++) {
                np[i][j] = f.apply(
                    i, j,
                    m1.get(i, j),
                    m2.get(m2.rows() == 1 ? 0 : i, m2.cols() == 1 ? 0 : j)
                );
            }
        }
        return Matrix.of(np);
    }

    private static boolean canZip(Matrix m1, Matrix m2) {
        return (m1.rows() == m2.rows() && m1.cols() == m2.cols()) ||
            (m2.rows() == 1 && m1.cols() == m2.cols()) ||
            (m2.cols() == 1 && m1.rows() == m2.rows());
    }

    public static String show(Matrix m) {
        StringBuilder lines = new StringBuilder();
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                lines.append(String.format("% 10.4f", m.get(i, j)));
            }
            lines.append('\n');
        }
        return lines.toString();
    }

    public static Matrix min(Matrix matrix, int axis) {
        if (axis == 0) {
            double[] np = new double[matrix.cols()];
            for (int i = 0; i < matrix.cols(); i++) {
                double min = Double.MAX_VALUE;
                for (int j = 0; j < matrix.rows(); j++) {
                    min = matrix.get(j, i) < min ? matrix.get(j, i) : min;
                }
                np[i] = min;
            }
            return new NaiveRowVector(np);
        }
        if (axis == 1) {
            double[] np = new double[matrix.rows()];
            for (int i = 0; i < matrix.rows(); i++) {
                double min = Double.MAX_VALUE;
                for (int j = 0; j < matrix.cols(); j++) {
                    min = matrix.get(i, j) < min ? matrix.get(i, j) : min;
                }
                np[i] = min;
            }
            return new NaiveColVector(np);
        }
        throw new IllegalArgumentException("invalid axis " + axis);
    }

    public static Matrix max(Matrix matrix, int axis) {
        if (axis == 0) {
            double[] np = new double[matrix.cols()];
            for (int i = 0; i < matrix.cols(); i++) {
                double max = Double.MIN_VALUE;
                for (int j = 0; j < matrix.rows(); j++) {
                    max = matrix.get(j, i) > max ? matrix.get(j, i) : max;
                }
                np[i] = max;
            }
            return new NaiveRowVector(np);
        }
        if (axis == 1) {
            double[] np = new double[matrix.rows()];
            for (int i = 0; i < matrix.rows(); i++) {
                double max = Double.MIN_VALUE;
                for (int j = 0; j < matrix.cols(); j++) {
                    max = matrix.get(i, j) > max ? matrix.get(i, j) : max;
                }
                np[i] = max;
            }
            return new NaiveColVector(np);
        }
        throw new IllegalArgumentException("invalid axis " + axis);
    }

}
