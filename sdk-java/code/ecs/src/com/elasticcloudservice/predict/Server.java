package com.elasticcloudservice.predict;

import java.util.ArrayList;
import java.util.List;

/**
 * server class
 */
public class Server {
    public int totalMem;
    public int totalCpu;
    public int freeMem;
    public int freeCpu;
    public List<Flavor> flavors;    // 物理服务器已放置虚拟机列表

    public Server(int totalCpu, int totalMem) {
        this.totalMem = totalMem;
        this.totalCpu = totalCpu;
        this.freeCpu = totalCpu;
        this.freeMem = totalMem;
        this.flavors = new ArrayList<>();
    }

    public boolean putFlavor(Flavor flavor) {
        if (freeCpu >= flavor.cpu && freeMem >= flavor.mem) {
            freeCpu -= flavor.cpu;
            freeMem -= flavor.mem;
            flavors.add(flavor);
            return true;
        }
        return false;
    }

    public double getCpuUsageRate() {
        return 1 - freeCpu / (double)totalCpu;
    }

    public double getMemUsageRate() {
        return 1 - freeMem / (double) totalMem;
    }
}
