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

        this.alpha = alpha;
        this.iteration = iteration;
        theta = new double[column - 1];

        initializeTheta();

        loadTrainData(dataset);
    }

    /**
     * 根据二维数组初始化
     * @param dataset
     * @param alpha
     * @param iteration
     */
    public LinearRegression(double[][] dataset, double alpha, int iteration) {
        this.row = dataset.length;
        this.column = dataset[0].length;
        trainData = dataset;

        this.alpha = alpha;
        this.iteration = iteration;
        theta = new double[column - 1];

        initializeTheta();

//        loadTrainData(dataset);
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
//        while ((iteration--) > 0) {
//            double[] partial_derivative = computePartialDerivative();   // 偏导数
//            for (int i = 0; i < theta.length; i++) {
//                theta[i] -= alpha * partial_derivative[i];
//            }
//        }
        for (int i = 0; i < iteration; i++) {
                // 输出代价
//            if (i % 100 == 0) {
//                System.out.println("Cost: " + computeCost(theta));
//            }
            double[] partial_derivative = computePartialDerivative();   // 偏导数
            for (int j = 0; j < theta.length; j++) {
                theta[j] -= alpha * partial_derivative[j];
            }
        }
    }

    private double computeCost(double[] theta) {
        double result = 0.0;
        for (int i = 0; i < trainData.length; i++) {
            double[] oneRow = trainData[i];
            double sum = 0;
            for (int k = 0; k < (oneRow.length -1); k++) {
                sum += theta[k] * oneRow[k];
            }
            result += Math.pow(sum - oneRow[oneRow.length - 1], 2);
        }
        return result;
    }

    private double[] computePartialDerivative() {
        double[] partial_derivative = new double[theta.length];
        for (int i = 0; i < theta.length; i++) {
            partial_derivative[i] = computePartialDerivativeForTheta(i);
        }
        return partial_derivative;
    }

    private double computePartialDerivativeForTheta(int thetaI) {
        double sum = 0.0;
        for (int j = 0; j < row; j++) {
            sum += h_theta_x_i_minus_y_i_times_x_j_i(j, thetaI);
        }
        return sum / row;
    }

    private double h_theta_x_i_minus_y_i_times_x_j_i(int r, int thetaI) {
        double[] oneRow = trainData[r];
        double result = 0.0;
        for (int k = 0; k < (oneRow.length -1); k++) {
            result += theta[k] * oneRow[k];
        }
        result -= oneRow[oneRow.length - 1];
        result *= oneRow[thetaI];
        return result;
    }

    private void initializeTheta() {
        for (int i = 0; i < theta.length; i++) {
            theta[i] = 0.0;     // 将theta各个参数初始化为1.0
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
        List<Integer> list3 = new ArrayList<>();
        list1.add(17);
        list1.add(25);
        list1.add(43);
        list1.add(26);
        list2.add(25);
        list2.add(43);
        list2.add(26);
        list2.add(31);
        list3.add(43);
        list3.add(26);
        list3.add(31);
        list3.add(31);
        dataset.add(list1);
        dataset.add(list2);
        dataset.add(list3);
        LinearRegression lr = new LinearRegression(dataset, 0.00001, 50);
        lr.trainTheta();
        lr.printTheta();
    }
}
