package io.ecs.deploy;

import com.elasticcloudservice.predict.Flavor;
import com.elasticcloudservice.predict.Server;

import java.util.*;

/**
 * 降序最佳适应法（BFD, Best Fit Decreasing）
 * 先对虚拟机按照优化资源进行排序，然后再按照最佳适应算法进行部署
 */
public class DeployBFD implements Deploy{
    @Override
    public List<Server> deploy(List<Flavor> predictFlavors, Server server, int optimization) {
        // 降序排列
        Collections.sort(predictFlavors, new Comparator<Flavor>() {
            @Override
            public int compare(Flavor o1, Flavor o2) {
                return optimization == 0 ? o2.cpu - o1.cpu : o2.mem - o1.mem;
            }
        });
        List<Server> servers = new ArrayList<Server>();
        servers.add(new Server(server.totalCpu, server.totalMem));
        for (int i = 0; i < predictFlavors.size(); i++) {
            Flavor oneflavor = predictFlavors.get(i);
            int minIndex = -1;
            for (int j = 0; j < servers.size(); j++) {
                Server temp = servers.get(j);
                int minLeave = Integer.MAX_VALUE;
                if (temp.freeCpu >= oneflavor.cpu && temp.freeMem >= oneflavor.mem) {
                    int leave = optimization == 0 ? temp.freeCpu - oneflavor.cpu : temp.freeMem - oneflavor.mem;
                    if (leave < minLeave) {
                        minLeave = leave;
                        minIndex = j;
                    }
                }
            }
            // 未找到满足虚拟机资源的物理服务器
            if (minIndex == -1) {
                Server newServer = new Server(server.totalCpu, server.totalMem);
                newServer.putFlavor(oneflavor);
                servers.add(newServer);
            } else {
                servers.get(minIndex).putFlavor(oneflavor);
            }
        }
        return servers;
    }
}
