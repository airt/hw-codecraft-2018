package io.ecs.common;

class NaiveMatrixOps {

    static Matrix add(Matrix lhs, Matrix rhs) {
        double[][] np = new double[lhs.rows()][lhs.cols()];
        if (lhs.cols() == rhs.cols()) {
            for (int i = 0; i < lhs.rows(); i++) {
                for (int j = 0; j < lhs.cols(); j++) {
                    np[i][j] = lhs.get(i, j) + rhs.get(rhs.rows() == 1 ? 0 : i, j);
                }
            }
        } else if (lhs.rows() == rhs.rows()) {
            for (int i = 0; i < lhs.rows(); i++) {
                for (int j = 0; j < lhs.cols(); j++) {
                    np[i][j] = lhs.get(i, j) + rhs.get(i, rhs.cols() == 1 ? 0 : j);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        return new NaiveMatrix(np);
    }

    static Matrix add(Matrix lhs, double b) {
        double[][] ans = new double[lhs.rows()][lhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < lhs.cols(); j++) {
                ans[i][j] = lhs.get(i, j) + b;
            }
        }
        return new NaiveMatrix(ans);
    }

    static Matrix sub(Matrix lhs, Matrix rhs) {
        if (lhs.cols() != rhs.cols())
            throw new IllegalArgumentException();
        double[][] np = new double[lhs.rows()][lhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < lhs.cols(); j++) {
                np[i][j] = lhs.get(i, j) - rhs.get(rhs.rows() == 1 ? 0 : i, j);
            }
        }
        return new NaiveMatrix(np);
    }

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

    static Matrix mul(Matrix lhs, double rhs) {
        double[][] np = new double[lhs.rows()][lhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < lhs.cols(); j++) {
                np[i][j] = lhs.get(i, j) * rhs;
            }
        }
        return new NaiveMatrix(np);
    }

    static Matrix dotMul(Matrix lhs, Matrix rhs) {
        double[][] np = new double[lhs.rows()][lhs.cols()];
        if (lhs.cols() == rhs.cols()) {
            for (int i = 0; i < lhs.rows(); i++) {
                for (int j = 0; j < lhs.cols(); j++) {
                    np[i][j] = lhs.get(i, j) * rhs.get(rhs.rows() == 1 ? 0 : i, j);
                }
            }
        } else if (lhs.rows() == rhs.rows()) {
            for (int i = 0; i < lhs.rows(); i++) {
                for (int j = 0; j < lhs.cols(); j++) {
                    np[i][j] = lhs.get(i, j) * rhs.get(i, rhs.cols() == 1 ? 0 : j);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        return new NaiveMatrix(np);
    }

    static Matrix dotDiv(Matrix lhs, Matrix rhs) {
        if (lhs.cols() != rhs.cols())
            throw new IllegalArgumentException();
        double[][] np = new double[lhs.rows()][lhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < lhs.cols(); j++) {
                np[i][j] = lhs.get(i, j) / rhs.get(rhs.rows() == 1 ? 0 : i, j);
            }
        }
        return new NaiveMatrix(np);
    }


    static Matrix dotDiv(Matrix lhs, double rhs) {
        double[][] np = new double[lhs.rows()][lhs.cols()];
        for (int i = 0; i < lhs.rows(); i++) {
            for (int j = 0; j < lhs.cols(); j++) {
                np[i][j] = lhs.get(i, j) / rhs;
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
