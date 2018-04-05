package io.ecs.common.matrix.impl;

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

    public static Matrix meanOfRows(Matrix m) {
        double[] means = new double[m.cols()];
        for (int j = 0; j < m.cols(); j++) {
            double sum = 0;
            for (int i = 0; i < m.rows(); i++) sum += m.get(i, j);
            double mean = sum / m.rows();
            means[j] = mean;
        }
        return RowVector.of(means);
    }

    public static Matrix meanAndStdOfRows(Matrix m) {
        if (m.rows() <= 1) throw new IllegalStateException();
        double[][] mass = new double[2][m.cols()];
        for (int j = 0; j < m.cols(); j++) {
            double sum = 0;
            for (int i = 0; i < m.rows(); i++) sum += m.get(i, j);
            double mean = sum / m.rows();
            mass[0][j] = mean;
        }
        for (int j = 0; j < m.cols(); j++) {
            double mean = mass[0][j];
            double sqrsum = 0;
            for (int i = 0; i < m.rows(); i++) sqrsum += Math.pow(m.get(i, j) - mean, 2);
            double std = Math.sqrt(sqrsum / (m.rows() - 1));
            mass[1][j] = std;
        }
        return new NaiveMatrix(mass);
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

    public static Matrix rowSum(Matrix m) {
        double[][] np = new double[m.rows()][1];
        for (int i = 0; i < m.rows(); i++) {
            double t = 0.0;
            for (int j = 0; j < m.cols(); j++) {
                t += m.get(i, j);
            }
            np[i][0] = t;
        }
        return new NaiveMatrix(np);
    }

    public static Matrix colSum(Matrix m) {
        double[][] np = new double[1][m.cols()];
        for (int i = 0; i < m.cols(); i++) {
            double t = 0.0;
            for (int j = 0; j < m.rows(); j++) {
                t += m.get(j, i);
            }
            np[0][i] = t;
        }
        return new NaiveMatrix(np);
    }

    public static Matrix map(Matrix m, IntIntDoubleToDoubleFunction f) {
        double[][] np = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i += 1) {
            for (int j = 0; j < m.cols(); j++) {
                np[i][j] = f.apply(i, j, m.get(i, j));
            }
        }
        return new NaiveMatrix(np);
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
        return new NaiveMatrix(np);
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

}
