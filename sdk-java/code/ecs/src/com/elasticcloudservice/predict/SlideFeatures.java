package com.elasticcloudservice.predict;

import com.filetool.util.Utils;
import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.Tuple2;
import io.ecs.common.matrix.impl.NaiveColVector;
import io.ecs.common.matrix.impl.NaiveRowVector;
import io.ecs.generator.DataGenerator;
import io.ecs.preprocessing.StandardScaler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据滑动窗口生成特征
 */
public class SlideFeatures implements DataGenerator{

    @Override
    public Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> generate(List<Record> history, List<Server> flavors, int T) {
        DataPre dp = new DataPre(history);
        int look_back = 5;
        Matrix nums = dp.nums();
        Matrix cpus = dp.cpus();
        Matrix mems = dp.mems();

        Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> data = new HashMap<>();

        for (Server flavor : flavors) {
            String name = flavor.getName();
            data.put(name, generateData(nums, cpus, mems, name, T, look_back));
        }

        return data;
    }

    Tuple2<Tuple2<Matrix, Matrix>, Matrix> generateData(Matrix nums,  Matrix cpus,  Matrix mems, String flavorName, int T, int look_back) {
        int flavorNum = Utils.getFlavorNum(flavorName);
        RowVector cpuOfDays = cpus.colSum();
        RowVector memOfDays = mems.colSum();

        NaiveRowVector totalCpuSlide = generateSlidesByTimes(cpuOfDays, T);
        NaiveRowVector totalMemSlide = generateSlidesByTimes(memOfDays, T);

        NaiveRowVector flavorCpuSlide = generateSlidesByTimes(cpus.row(flavorNum - 1), T);
        NaiveRowVector flavorMemSlide = generateSlidesByTimes(mems.row(flavorNum - 1), T);
        NaiveRowVector flavorSlide = generateSlidesByTimes(nums.row(flavorNum - 1), T);

        Matrix totalCpuFea = generateFeatures(totalCpuSlide, look_back);
        Matrix totalMemFea = generateFeatures(totalMemSlide, look_back);
        Matrix flavorFea = generateFeatures(flavorSlide, look_back);
        Matrix flavorCpuFea = generateFeatures(flavorCpuSlide, look_back);
        Matrix flavorMemFea = generateFeatures(flavorMemSlide, look_back);

        Matrix flavorCpuRate = flavorCpuFea.dotDiv(totalCpuFea);
        Matrix flavorMemRate = flavorMemFea.dotDiv(totalMemFea);

        int m = totalCpuFea.rows();
        int end = m - T - 1;
//        Matrix total = totalCpuFea.concatenateH(totalMemFea).concatenateH(flavorFea).concatenateH(flavorCpuRate).concatenateH(flavorMemRate);
//        Matrix total = flavorFea.concatenateH(flavorCpuRate).concatenateH(flavorMemRate);
        Matrix total = flavorFea;

        total = total.concatenate(flavorFea.mean(1), 1);
//        total = total.concatenate(flavorFea.min(1), 1);
//        total = total.concatenate(flavorFea.max(1), 1);

        total = total.t();
//        StandardScaler scaler = new StandardScaler();
//        scaler.fit(total);
//        total = scaler.transform(total);

        Matrix X = total.cols(0, end);
//        System.out.println(X.t().show());

        RowVector Y = generateY(flavorSlide, look_back, T);

//        System.out.println(Y.show());
        Matrix predictX = total.col(-1);
        return Tuple2.of(Tuple2.of(X, predictX), Y);
    }

    public RowVector generateY(RowVector orig, int look_back, int t) {
        return orig.cols(look_back + t - 1, -1).colSum();
    }

    public Matrix generateFeatures(RowVector orig, int look_back) {
        double[][] np = new double[orig.cols() - look_back + 1][look_back];
        for (int i = 0; i < np.length; i++) {
            for (int j = i; j < i + look_back; j++) {
                np[i][j - i] = orig.get(0, j);
            }
        }
        return Matrix.of(np);
    }

    /**
     * 根据天数t在原始数据中生成特征（sum）
     */
    public NaiveRowVector generateSlidesByTimes(RowVector orig, int t) {
        double[] np = new double[orig.cols() - t + 1];
        for (int j = 0; j <= orig.cols() - t; j++) {
            double sum = 0.0;
            for (int k = j; k < j + t; k++) {
                sum += orig.get(0, k);
            }
            np[j] = sum;
        }
        return new NaiveRowVector(np);
    }
}
