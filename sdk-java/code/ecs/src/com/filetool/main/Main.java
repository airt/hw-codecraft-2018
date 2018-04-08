package com.filetool.main;

import com.elasticcloudservice.predict.Input;
import com.elasticcloudservice.predict.Predict;
import com.elasticcloudservice.predict.Server;
import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;
import com.model.Metrics;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
		// 修改为自己电脑上的目录
        String base = "";
        String ecsDataPath = base + "train.txt";
        String inputFilePath = base + "input.txt";
        String testDataPath = base + "test.txt";
        String resultFilePath = base + "result.txt";

        LogUtil.printLog("Begin");

        // 读取输入文件
        String[] ecsContent = FileUtil.read(ecsDataPath, null);
        String[] inputContent = FileUtil.read(inputFilePath, null);
        String[] testContent = FileUtil.read(testDataPath, null);
        // 功能实现入口
        String[] resultContents = Predict.predictVm(ecsContent, inputContent);

        Input input = new Input(inputContent);

        HashMap<Server, Integer> predictFlavors = generatePredict(input.getFlavors(), resultContents);
        HashMap<Server, Integer> actualFlavors = generateActual(input.getFlavors(), testContent);
        double predictScore = Metrics.predictScore(predictFlavors, actualFlavors);
        int physicalNum = Integer.valueOf(resultContents[predictFlavors.size() + 2]);
        double deplyScore =  Metrics.deployScore(predictFlavors, input.getPhysical(), physicalNum, input.getOptimization());

        LogUtil.printLog("Predict score: " + predictScore);
        LogUtil.printLog("Deploy score: " + deplyScore);
        LogUtil.printLog("Score: " + predictScore * deplyScore * 100);

        // 写入输出文件
        if (hasResults(resultContents)) {
            FileUtil.write(resultFilePath, resultContents, false);
        } else {
            FileUtil.write(resultFilePath, new String[]{"NA"}, false);
        }

        LogUtil.printLog("End");
    }

    public static HashMap<Server, Integer> generateActual(List<Server> flavors, String[] testContent) {
        HashMap<Server, Integer> actualFlavors = new HashMap<>();
        for (Server flavor : flavors) {
            actualFlavors.put(flavor, 0);
        }
        for (String line : testContent) {
            for (Server server : flavors) {
                if (line.split("\\t")[1].equals(server.getName())) {
                    actualFlavors.put(server, actualFlavors.get(server) + 1);
                }
            }
        }
        return actualFlavors;
    }

    public static HashMap<Server,Integer> generatePredict(List<Server> flavors, String[] resultContents) {
        HashMap<Server, Integer> predictFlavors = new HashMap<>();
        int nums = flavors.size();
        for (int i = 1; i < nums + 1; i++) {
            for (Server server : flavors) {
                if (resultContents[i].split(" ")[0].equals(server.getName())) {
                    predictFlavors.put(server,  Integer.valueOf(resultContents[i].split(" ")[1]));
                }
            }
        }
        return predictFlavors;
    }

    private static boolean hasResults(String[] resultContents) {
        if (resultContents == null) {
            return false;
        }
        for (String contents : resultContents) {
            if (contents != null && !contents.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
