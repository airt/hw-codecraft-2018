package com.elasticcloudservice.predict;

/**
 * 物理服务器即虚拟机，可考虑分开
 */
public class Server {
    public String name;
    public int cpu;
    public int mem;

    public Server(String name, int cpu, int mem) {
        this.name = name;
        this.cpu = cpu;
        this.mem = mem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMem() {
        return mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }
}
