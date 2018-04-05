package io.ecs.common;

import io.ecs.common.function.IntIntDoubleDoubleToDoubleFunction;
import io.ecs.common.function.IntIntDoubleToDoubleFunction;

class NaiveMatrixOps {

    static Matrix mul(Matrix lhs, Matrix rhs) {
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

    static Matrix map(Matrix m, IntIntDoubleToDoubleFunction f) {
        double[][] np = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i += 1) {
            for (int j = 0; j < m.cols(); j++) {
                np[i][j] = f.apply(i, j, m.get(i, j));
            }
        }
        return new NaiveMatrix(np);
    }

    static Matrix zip(Matrix m1, Matrix m2, IntIntDoubleDoubleToDoubleFunction f) {
        if (m1.rows() != m2.rows() && m1.cols() != m2.cols())
            throw new IllegalArgumentException();
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

    static double sum(Matrix m) {
        double re = 0.0;
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                re += m.get(i, j);
            }
        }
        return re;
    }

    static Matrix rowSum(Matrix m) {
        double[][] np = new double[m.rows()][1];
        for (int i = 0; i < m.rows(); i++) {
            double sum = 0.0;
            for (int j = 0; j < m.cols(); j++) {
                sum += m.get(i, j);
            }
            np[i][0] = sum;
        }
        return new NaiveMatrix(np);
    }

    static Matrix colSum(Matrix m) {
        double[][] np = new double[1][m.cols()];
        for (int i = 0; i < m.cols(); i++) {
            double sum = 0.0;
            for (int j = 0; j < m.rows(); j++) {
                sum += m.get(j, i);
            }
            np[0][i] = sum;
        }
        return new NaiveMatrix(np);
    }

}
