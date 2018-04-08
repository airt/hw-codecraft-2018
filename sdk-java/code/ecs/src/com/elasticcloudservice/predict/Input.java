package com.elasticcloudservice.predict;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入信息类
 */
public class Input {

    private Server physical;
    private List<Server> flavors;
    private int optimization;
    private String startTime;
    private String endTime;

    public Input(String[] inputContent) {
        this.physical = new Server("pysical", Integer.valueOf(inputContent[0].split(" ")[0]), Integer.valueOf(inputContent[0].split(" ")[1]));
        int flavorNum = Integer.valueOf(inputContent[2]);
        this.flavors = new ArrayList<>();
        for (int i = 3; i < 3 + flavorNum; i++) {
            String[] array = inputContent[i].split(" ");
            Server flavor = new Server(array[0], Integer.valueOf(array[1]), Integer.valueOf(array[2]) / 1024);
            this.flavors.add(flavor);
        }
        // 优化资源名称，0：CPU, 1: MEM
        this.optimization = inputContent[4 + flavorNum].equals("CPU") ? 0 : 1;
        // 预测开始时间和结束时间
        this.startTime = inputContent[6 + flavorNum];
        this.endTime = inputContent[7 + flavorNum];
    }

    public Server getPhysical() {
        return physical;
    }

    public List<Server> getFlavors() {
        return flavors;
    }

    public int getOptimization() {
        return optimization;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
