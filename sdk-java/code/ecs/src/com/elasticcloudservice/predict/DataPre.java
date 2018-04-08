package com.elasticcloudservice.predict;

import com.filetool.util.Utils;
import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 历史数据预处理
 */
public class DataPre {

    /**
     * 各种虚拟机规格每天数量矩阵
     */
    private Matrix nums;
    /**
     * 各种虚拟机规格每天cpu总和矩阵
     */
    private Matrix cpus;
    /**
     * 各种虚拟机规格每天mem总和矩阵
     */
    private Matrix mems;

    private int rows;

    public DataPre(List<Record> history) {
        List<String> dates = getPeriodDate(history);
        Map<String, List<Record>> dateRecord = getDayRecord(dates, history);
        this.rows = maxFlavor(history);
        this.nums = generateMatrixNums(dates, dateRecord);
        Tuple2<Matrix, Matrix> t2 = generateMatrixCpusMems(nums);
        this.cpus = t2._1();
        this.mems = t2._2();
    }

    private Tuple2<Matrix, Matrix> generateMatrixCpusMems(Matrix nums) {
        double[][] cpus = new double[nums.rows()][nums.cols()];
        double[][] mems = new double[nums.rows()][nums.cols()];
        for (int i = 0; i < nums.rows(); i++) {
            for (int j = 0; j < nums.cols(); j++) {
                double[] cpuMem = Utils.getFlavorCpuMem(i + 1);
                cpus[i][j] = cpuMem[0] * nums.get(i, j);
                mems[i][j] = cpuMem[1] * nums.get(i, j);
            }
        }
        return Tuple2.of(Matrix.of(cpus), Matrix.of(mems));
    }

    private Matrix generateMatrixNums(List<String> dates, Map<String, List<Record>> dateRecord) {
        double[][] nums = new double[rows][dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < nums.length; j++) {
                double count = 0.0;
                for (Record record : dateRecord.get(dates.get(i))) {
                    count += Utils.getFlavorNum(record.getFlavorName()) == (j + 1) ? 1 : 0;
                }
                nums[j][i] = count;
            }
        }
        return Matrix.of(nums);
    }

    public Matrix nums() {
        return this.nums;
    }

    public Matrix cpus() {
        return this.cpus;
    }

    public Matrix mems() {
        return this.mems;
    }

    public int maxFlavor(List<Record> history) {
        int max = 0;
        for (Record recod : history) {
            int temp = Utils.getFlavorNum(recod.getFlavorName());
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    private Map<String, List<Record>> getDayRecord(List<String> periodDate, List<Record> history) {
        Map<String, List<Record>> historyMap = new HashMap<>();
        for (int i = 0; i < periodDate.size(); i++) {
            List<Record> temp = new ArrayList<>();
            for (int j = 0; j < history.size(); j++) {
                if (history.get(j).getCreateTime().equals(periodDate.get(i))) {
                    temp.add(history.get(j));
                }
            }
            historyMap.put(periodDate.get(i), temp);
        }
        return historyMap;
    }

    private List<String> getPeriodDate(List<Record> history) {
        String startDate = history.get(0).getCreateTime().split(" ")[0];
        String endDate = history.get(history.size()-1).getCreateTime().split(" ")[0];
        return collectLocalDates(startDate, endDate);
    }

    private List<String> collectLocalDates(String timeStart, String timeEnd){
        return collectLocalDates(LocalDate.parse(timeStart), LocalDate.parse(timeEnd));
    }

    private List<String> collectLocalDates(LocalDate start, LocalDate end) {
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }
}
