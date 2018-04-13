package com.model;

import com.elasticcloudservice.predict.Flavor;
import com.elasticcloudservice.predict.Server;

import java.util.Map;

/**
 * 预测评分计算
 * http://codecraft.devcloud.huaweicloud.com/home/detail
 * @author cutoutsy@gmail.com
 */
public class Metrics {
    /**
     * 计算预测的希尔系数
     * @param flavorsNum    需要预测规格虚拟机的实际数量
     * @param predictFlavors    需要预测规格虚拟机的预测数量
     * @return
     */
    public static double hill(Map<Flavor, Integer> flavorsNum, Map<Flavor, Integer> predictFlavors) {
        // 虚拟机规格的集合
        double n = flavorsNum.size();
        double sum = 0.0;
        double actualSum = 0.0;
        double predictSum = 0.0;
        for (Flavor flavor : flavorsNum.keySet()) {
            sum += Math.pow(flavorsNum.get(flavor) - predictFlavors.get(flavor), 2);
            actualSum += Math.pow(flavorsNum.get(flavor), 2);
            predictSum += Math.pow(predictFlavors.get(flavor), 2);
        }
        // 计算分子
        double numerator = Math.sqrt(sum / n);
        // 计算分母
        double denominator = Math.sqrt(actualSum / n) + Math.sqrt(predictSum / n);

        return numerator / denominator;
    }

    /**
     * 预测部分得分
     * @param flavorsNum
     * @param predictFlavors
     * @return
     */
    public static double predictScore(Map<Flavor, Integer> flavorsNum, Map<Flavor, Integer> predictFlavors) {
        return 1 - hill(flavorsNum, predictFlavors);
    }

    /**
     * 虚拟机预测后，虚拟机分配得分计算
     * @param predictFlavors
     * @param physicalNum   物理服务器数目
     * @param optimization 需要优化的资源（0：cpu, 1: Mem）
     * @return
     */
    public static double deployScore(Map<Flavor, Integer> predictFlavors, Server server, int physicalNum, int optimization) {
        double denominator = optimization == 0 ? server.totalCpu * physicalNum : server.totalMem * physicalNum;
        double numerator = 0.0;
        for (Flavor flavor : predictFlavors.keySet()) {
            numerator += (optimization == 0 ? flavor.cpu : flavor.mem) * predictFlavors.get(flavor);
        }
        return numerator / denominator;
    }

    /**
     * 计算最后总的得分
     * @param flavorsNum
     * @param predictFlavors
     * @param physical
     * @param physicalNum
     * @param optimization
     * @return
     */
    public static double score(Map<Flavor, Integer> flavorsNum, Map<Flavor, Integer> predictFlavors, Server physical, int physicalNum, int optimization) {
        return predictScore(flavorsNum, predictFlavors) * deployScore(predictFlavors, physical, physicalNum, optimization);
    }
}
