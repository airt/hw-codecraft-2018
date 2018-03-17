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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (cpu != server.cpu) return false;
        if (mem != server.mem) return false;
        return name != null ? name.equals(server.name) : server.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + cpu;
        result = 31 * result + mem;
        return result;
    }
}
