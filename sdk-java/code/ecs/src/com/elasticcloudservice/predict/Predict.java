package com.elasticcloudservice.predict;

import com.filetool.util.Utils;
import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import io.ecs.deploy.DeployBFD;
import io.ecs.model.LinearRegression;
import io.ecs.model.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Predict {

    public static String[] predictVm(String[] ecsContent, String[] inputContent) {

        List<Record> history = generateHistory(ecsContent);

        Input input = new Input(inputContent);

        Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> data  = createTrainData(history, input.getFlavors(), input.getStartTime(), input.getEndTime());

        Model model = new LinearRegression(0.001, 10000);

        HashMap<Server, Integer> predictRe = predict(model, data);

        List<Map<Server, Integer>> deployRe = deploy(predictRe, input.getPhysical(), input.getOptimization());

        return generateResults(predictRe, deployRe);
    }

    private static Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> createTrainData(List<Record> history, List<Server> flavors, String start, String end) {
        int period = (int)ChronoUnit.DAYS.between(LocalDate.parse(start.split(" ")[0]), LocalDate.parse(end.split(" ")[0]));
        SlideFeatures dataGenerator = new SlideFeatures();
        return dataGenerator.generate(history, flavors, period);
    }

    public static String[] generateResults(HashMap<Server, Integer> predictRe, List<Map<Server, Integer>> deployRe) {
        int flavorNum = predictRe.values().stream().reduce(0, (acc, element) -> acc + element);
        int physicalNum = deployRe.size();
        String[] results = new String[predictRe.size() + physicalNum + 3];
        results[0] = String.valueOf(flavorNum);
        int resultIndex = 1;
        for (Server item : predictRe.keySet()) {
            results[resultIndex++] = item.getName() + " " + predictRe.get(item);
        }
        results[resultIndex++] = "";
        results[resultIndex++] = String.valueOf(physicalNum);

        for (int i = 0; i < deployRe.size(); i++) {
            StringBuilder sb = new StringBuilder(i+1 + " ");
            for (Server server : deployRe.get(i).keySet()) {
                sb.append(server.getName() + " " + deployRe.get(i).get(server) + " ");
            }
            results[resultIndex++] = sb.toString().trim();
        }
        return results;
    }

    private static List<Map<Server, Integer>> deploy(HashMap<Server, Integer> predictRe, Server physical, int optimization) {
        DeployBFD deployBFD = new DeployBFD();
        List<Server> predictFlavors = new LinkedList<>();
        for (Server flavor : predictRe.keySet()) {
            for (int i = 0; i < predictRe.get(flavor); i++) {
                predictFlavors.add(flavor);
            }
        }
        return deployBFD.deploy(predictFlavors, physical, optimization);
    }

    public static HashMap<Server, Integer> predict(Model model, Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> data) {
        HashMap<Server, Integer> re = new HashMap<>();
//        LinearRegression model = new LinearRegression(0.001, 100000);
        for (String flavor : data.keySet()) {
            Matrix X = data.get(flavor)._1()._1();
            Matrix predictX = data.get(flavor)._1()._2();
            Matrix Y = data.get(flavor)._2();
            model.fit(X, Y);
            Matrix result = model.predict(predictX);

            double[] cpuMem = Utils.getFlavorCpuMem(Utils.getFlavorNum(flavor));
            Server s = new Server(flavor, (int)cpuMem[0], (int)cpuMem[1]);

            double num = result.get(0, 0);
            re.put(s, num < 0 ? 0 : (int) Math.round(num));
        }
        return re;
    }

    public static List<Record> generateHistory(String[] ecsContent) {
        List<Record> history = new ArrayList<Record>();

        for (int i = 0; i < ecsContent.length; i++) {
            if (ecsContent[i].contains(" ") && ecsContent[i].split("\\t").length == 3) {
                String[] array = ecsContent[i].split("\\t");
                history.add(new Record(array[0], array[1], array[2].split(" ")[0]));
            }
        }
        return history;
    }
}
