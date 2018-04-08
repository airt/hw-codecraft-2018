package com.filetool.util;

/**
 * 工具类
 */
public class Utils {
    /**
     * 根据虚拟机规格数字获取cpu、mem数
     */
    public static double[] getFlavorCpuMem(double num) {
        double cpus = Math.pow(2.0, Math.ceil(num / 3) - 1);
        double mems = cpus * (num % 3 == 0 ? 4 : num % 3);
        return new double[] {cpus, mems};
    }

    /**
     * 获取虚拟机规格数字
     * @param flavorName
     * @return
     */
    public static int getFlavorNum(String flavorName) {
        return Integer.valueOf(flavorName.substring(6));
    }
}
