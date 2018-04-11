package com.elasticcloudservice.predict;

/**
 * Flavor Class
 */
public class Flavor {
    public String name;
    public int mem;
    public int cpu;

    public Flavor(String name, int cpu, int mem) {
        this.name = name;
        this.mem = mem;
        this.cpu = cpu;
    }

    public Flavor() {
    }
}
