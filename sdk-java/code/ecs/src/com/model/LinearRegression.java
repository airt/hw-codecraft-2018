package com.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 多元线性回归
 * @author cutoutsy@gmail.com
 */
public class LinearRegression {
    private double[][] trainData;   // 训练数据
    private int row;    // 训练数据 行数
    private int column;     // 训练数据 列数

    private double[] theta;        // 参数theta

    private double alpha;   // 训练步长
    private int iteration;      // 迭代次数

    public LinearRegression(List<List<Integer>> dataset, double alpha, int iteration) {
        this.row = dataset.size();
        this.column = dataset.get(0).size();
        trainData = new double[row][column];

        this.alpha = alpha; // 步长默认为0.001
        this.iteration = iteration;     // 迭代次数为1w
        theta = new double[column - 1];

        initializeTheta();

        loadTrainData(dataset);
    }

    private void loadTrainData(List<List<Integer>> dataset) {
//        for (int i = 0; i < row; i++) {
//            trainData[i][0] = 1.0;      // trainData的第一列全部置为1.0（feature x0）
//        }
        for (int i = 0; i < dataset.size(); i++) {
            for (int j = 0; j < dataset.get(i).size(); j++) {
                trainData[i][j] = dataset.get(i).get(j);
            }
        }
    }

    public void trainTheta() {
        int iteration = this.iteration;
        while ((iteration--) > 0) {
            double[] partial_derivative = computePartialDerivative();   // 偏导数
            for (int i = 0; i < theta.length; i++) {
                theta[i] -= alpha * partial_derivative[i];
            }
        }
    }

    private double[] computePartialDerivative() {
        double[] partial_derivative = new double[theta.length];
        for (int i = 0; i < theta.length; i++) {
            partial_derivative[i] = computePartialDerivativeForTheta(i);
        }
        return partial_derivative;
    }

    private double computePartialDerivativeForTheta(int i) {
        double sum = 0.0;
        for (int j = 0; j < row; j++) {
            sum += h_theta_x_i_minus_y_i_times_x_j_i(j, i);
        }
        return sum / row;
    }

    private double h_theta_x_i_minus_y_i_times_x_j_i(int j, int i) {
        double[] oneRow = trainData[j];
        double result = 0.0;
        for (int k = 0; k < (oneRow.length -1); k++) {
            result += theta[k] * oneRow[k];
        }
        result -= oneRow[oneRow.length - 1];
        result *= oneRow[i];
        return result;
    }

    private void initializeTheta() {
        for (int i = 0; i < theta.length; i++) {
            theta[i] = 1.0;     // 将theta各个参数初始化为1.0
        }
    }

    public void printTheta() {
        for (double a : theta) {
            System.out.println(a + " ");
        }
    }

    public double[] getTheta() {
        return theta;
    }

    public static void main(String[] args) {
        List<List<Integer>> dataset = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(1);
        list1.add(5);
        list2.add(1);
        list2.add(2);
        list2.add(8);
        dataset.add(list1);
        dataset.add(list2);
        LinearRegression lr = new LinearRegression(dataset, 0.01, 10000);
        lr.trainTheta();
        lr.printTheta();
    }
}
