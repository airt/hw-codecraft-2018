package com.elasticcloudservice.predict;

import com.filetool.util.FileUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据历史数据生成特征
 */
public class Feature {

    public static Map<Server, List<List<Integer>>> createTrainData(List<Record> history, Map<Server, Integer> predictFlavors, int predictPeriod, int look_back) {
       return Feature.createFeature(Feature.createPredictPeriod(Feature.preProcess(history, predictFlavors), predictPeriod), look_back);
    }
    public static Map<Server, List<Integer>> preProcess(List<Record> history, Map<Server, Integer> predictFlavors) {
        Map<Server, List<Record>> predictFlavorRecord = new HashMap<>();
        for (Server server : predictFlavors.keySet()) {
            predictFlavorRecord.put(server, history.stream().filter(record -> record.getFlavorName().equals(server.getName())).collect(Collectors.toList()));
        }
        String startDate = history.get(0).getCreateTime().split(" ")[0];
        String endDate = history.get(history.size()-1).getCreateTime().split(" ")[0];
        List<String> periodDate = collectLocalDates(startDate, endDate);
        Map<Server, List<Integer>> flavorDayCount = new HashMap<>();
        for (Server server : predictFlavorRecord.keySet()) {
            List<Record> oneServer = predictFlavorRecord.get(server);
            int index = 0;
            for (int i = 0; i < periodDate.size(); i++) {
                int count = 0;
                while (index < oneServer.size() && oneServer.get(index).getCreateTime().split(" ")[0].equals(periodDate.get(i))) {
                    count++;
                    index++;
                }
                if (flavorDayCount.keySet().contains(server)) {
                    List<Integer> temp = flavorDayCount.get(server);
                    temp.add(count);
                    flavorDayCount.put(server, temp);
                } else {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(count);
                    flavorDayCount.put(server, temp);
                }
            }
        }
//        for (Server server : flavorDayCount.keySet()) {
//            System.out.println(server.getName() + ":" + flavorDayCount.get(server).toString());
//        }
        return flavorDayCount;
    }

    /**
     * 以预测周期统计各个flavor在历史训练数据的数量
     * @param flavorDayCount
     * @param predictPeriod    预测周期（1到2周）:天为单位
     * @return
     */
    public static Map<Server, List<Integer>> createPredictPeriod(Map<Server, List<Integer>> flavorDayCount, int predictPeriod) {
        int len = 0;
        for (Server server : flavorDayCount.keySet()) {
            len = flavorDayCount.get(server).size();
            break;
        }
        Map<Server, List<Integer>> flavorPredictPeriodCount = new HashMap<>();
        int offset = len % predictPeriod;
        for (Server server : flavorDayCount.keySet()) {
            List<Integer> oneServerDate = flavorDayCount.get(server);
            for (int i = offset; i < len; i = i + predictPeriod) {
                int sum = 0;
                for (int j = 0; j < predictPeriod; j++) {
                    sum += oneServerDate.get(i+j);
                }
                if (flavorPredictPeriodCount.keySet().contains(server)) {
                    List<Integer> temp = flavorPredictPeriodCount.get(server);
                    temp.add(sum);
                    flavorPredictPeriodCount.put(server, temp);
                } else {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(sum);
                    flavorPredictPeriodCount.put(server, temp);
                }
            }
        }
//        for (Server server : flavorPredictPeriodCount.keySet()) {
//            System.out.println(server.getName() + ":" + flavorPredictPeriodCount.get(server).toString());
//        }
        return flavorPredictPeriodCount;
    }

    // look_back表示步长
    public static Map<Server, List<List<Integer>>> createFeature(Map<Server, List<Integer>> flavorPredictPeriodCount, int look_back) {
        int len = 0;
        for (Server server : flavorPredictPeriodCount.keySet()) {
            len = flavorPredictPeriodCount.get(server).size();
            break;
        }
        Map<Server, List<List<Integer>>> re = new HashMap<>();
        for (Server server : flavorPredictPeriodCount.keySet()) {
            List<Integer> oneServerDate = flavorPredictPeriodCount.get(server);
            List<List<Integer>> oneServerSamples = new ArrayList<>();
            for (int i = 0; i <= (len - look_back - 1); i++) {
                List<Integer> sample = new ArrayList<>();
                for (int j = 0; j <= look_back; j++) {
                    sample.add(oneServerDate.get(i + j));
                }
                oneServerSamples.add(sample);
            }
            re.put(server, oneServerSamples);
        }
        // 将训练数据写入到文件中，以便分析
        for (Server server : re.keySet()) {
            String[] content = new String[re.get(server).size()];
            for (int i = 0; i < re.get(server).size(); i++) {
                content[i] = re.get(server).get(i).toString();
            }
            for (int i = 0; i < content.length; i++) {
                content[i] = content[i].substring(1, content[i].length() - 1);
            }
//            FileUtil.write("/Users/cutoutsy/workspace/codecraft/data/example/case0/" + server.getName() + ".txt", content, false);
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

    public static void main(String[] args) {
        String timeStart = "2016-12-11 19:03:32";
        String timeEnd = "2016-12-20 19:03:32";

        System.out.println(ChronoUnit.DAYS.between(LocalDate.parse(timeStart), LocalDate.parse(timeEnd)));
    }
}
