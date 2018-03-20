package com.elasticcloudservice.predict;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import io.ecs.generator.DataGenerator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据滑动窗口生成特征
 */
public class SlideWindowFeatures implements DataGenerator{

    @Override
    public Map<String, Tuple2<Matrix, Matrix>> generate(List<Record> history, List<Server> flavors, int T) {
        String startDate = history.get(0).getCreateTime().split(" ")[0];
        String endDate = history.get(history.size()-1).getCreateTime().split(" ")[0];
        List<String> periodDate = collectLocalDates(startDate, endDate);

        Map<String, List<Record>> historyMap = new HashMap<>();

        // 统计每天的cpu、mem信息
        double[][] cpuMems = new double[2][periodDate.size()];

        for (int i = 0; i < periodDate.size(); i++) {
            List<Record> temp = new ArrayList<>();
            for (int j = 0; j < history.size(); j++) {
                if (history.get(j).getCreateTime().equals(periodDate.get(i))) {
                    temp.add(history.get(j));
                }
            }
            historyMap.put(periodDate.get(i), temp);
        }
        for (int i = 0; i < cpuMems.length; i++) {
            for (int j = 0; j < periodDate.size(); j++) {
                List<Record> oneDayRecord = historyMap.get(periodDate.get(j));
                double sum = 0.0;
                for (int k = 0; k < oneDayRecord.size(); k++) {
                    sum += getFlavorCpuMem(oneDayRecord.get(k).getFlavorName())[i];
                }
                cpuMems[i][j] = sum;
            }
        }

        Map<String, double[]> flavorsData = new HashMap<>();
        for (int i = 0; i < flavors.size(); i++) {
            String flavor = flavors.get(i).getName();
            double[] flavorCount = new double[periodDate.size()];
            for (int j = 0; j < periodDate.size(); j++) {
                List<Record> oneDayRecord = historyMap.get(periodDate.get(j));
                double count = 0.0;
                for (int k = 0; k < oneDayRecord.size(); k++) {
                    if (oneDayRecord.get(k).getFlavorName().equals(flavor)) {
                        count++;
                    }
                }
                flavorCount[j] = count;
            }
            flavorsData.put(flavor, flavorCount);
        }

        // 按周期滑动后统计
        double[][] cpuMemSlide = new double[2][cpuMems[0].length - T + 1];
        for (int i = 0; i < cpuMems.length; i++) {
            for (int j = 0; j <= cpuMems[i].length - T; j++) {
                double sum = 0.0;
                for (int k = j; k < j + T; k++) {
                    sum += cpuMems[i][k];
                }
                cpuMemSlide[i][j] = sum;
            }
        }

        for (String flavor : flavorsData.keySet()) {
            double[] flavorData = flavorsData.get(flavor);
            double[] slide = new double[flavorData.length - T + 1];
            for (int i = 0; i <= flavorData.length - T; i++) {
                double sum = 0.0;
                for (int j = i; j < i + T; j++) {
                    sum += flavorData[j];
                }
                slide[i] = sum;
            }
            flavorsData.put(flavor, slide);
        }

        Map<String, double[][]> results = new HashMap<>();
        int look_back = 4;
        int row = cpuMemSlide[0].length - look_back - T + 1;
        double[][] features = new double[row][look_back * 5];
//        int colIndex = 0;
        for (int i = 0; i < cpuMemSlide.length; i++) {
            for (int j = 0; j <= cpuMemSlide[i].length - look_back - T; j++) {
                for (int k = 0; k < look_back; k++) {
                    features[j][i * look_back + k] = cpuMemSlide[i][k + j];
                }
            }
        }

        for (int i = 0; i < flavors.size(); i++) {
            double[][] copy = new double[features.length][];
            for (int j = 0; j < features.length; j++) {
                copy[j] = features[j].clone();
            }
            results.put(flavors.get(i).getName(), copy);
        }

        for (String flavor : flavorsData.keySet()) {
            double[][] oneFeature = results.get(flavor);
            double[] flavorCpuMem = getFlavorCpuMem(flavor);
            double[] oneData = flavorsData.get(flavor);
            for (int i = 0; i <= oneData.length - look_back - T; i++) {
                for (int j = 0; j < look_back; j++) {
                    oneFeature[i][8 + j] = oneData[i+j];
                    oneFeature[i][12 + j] = oneData[i+j] == 0 || oneFeature[i][j] == 0 ? 0.0 : oneData[i+j] * flavorCpuMem[0] / oneFeature[i][j];
                    oneFeature[i][16 + j] = oneData[i+j] == 0 || oneFeature[i][4 + j] == 0 ? 0.0 : oneData[i+j] * flavorCpuMem[1] / oneFeature[i][4 + j];
                }
            }
        }

        Map<String, double[][]> labels = new HashMap<>();
        for (String flavor : flavorsData.keySet()) {
            double[] oneData = flavorsData.get(flavor);
            double[][] label = new double[oneData.length - look_back - T + 1][1];
            for (int i = look_back + T - 1; i < oneData.length; i++) {
                label[i + 1 - look_back - T][0] = oneData[i];
            }
            labels.put(flavor, label);
        }

        Map<String, Tuple2<Matrix, Matrix>> re = new HashMap<>();
        for (String flavor : results.keySet()) {
            Matrix fea = Matrix.of(results.get(flavor));
            Matrix lab = Matrix.of(labels.get(flavor));
            re.put(flavor, Tuple2.of(fea, lab));
        }
        
        return re;
    }

    public static List<String> collectLocalDates(String timeStart, String timeEnd){
        return collectLocalDates(LocalDate.parse(timeStart), LocalDate.parse(timeEnd));
    }

    public static List<String> collectLocalDates(LocalDate start, LocalDate end) {
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    /**
     * 根据虚拟机名字获取cpu、mem数
     * @param flavorName
     * @return
     */
    public static double[] getFlavorCpuMem(String flavorName) {
        double num = (double) Integer.valueOf(flavorName.substring(6));
        double cpus = Math.pow(2.0, Math.ceil(num / 3) - 1);
        double mems = cpus * (num % 3 == 0 ? 4 : num % 3);
        return new double[] {cpus, mems};
    }

}
