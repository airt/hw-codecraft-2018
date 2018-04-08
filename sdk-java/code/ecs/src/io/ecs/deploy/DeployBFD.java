package io.ecs.deploy;

import com.elasticcloudservice.predict.Server;

import java.util.*;

/**
 * 降序最佳适应法（BFD, Best Fit Decreasing）
 * 先对虚拟机按照优化资源进行排序，然后再按照最佳适应算法进行部署
 */
public class DeployBFD implements Deploy{

    @Override
    public List<Map<Server, Integer>> deploy(List<Server> predictFlavors, Server physical, int optimization) {
        // 降序排列
        Collections.sort(predictFlavors, new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return optimization == 0 ? o2.getCpu() - o1.getCpu() : o2.getMem() - o1.getMem();
            }
        });
        List<Server> servers = new ArrayList<Server>();
        Map<Integer, Map<Server, Integer>> result = new HashMap<>();
        int serverId = 1;
        servers.add(new Server(String.valueOf(serverId++), physical.getCpu(), physical.getMem()));
        for (int i = 0; i < predictFlavors.size(); i++) {
            Server oneflavor = predictFlavors.get(i);
            int minIndex = -1;
            for (int j = 0; j < servers.size(); j++) {
                Server temp = servers.get(j);
                int minLeave = Integer.MAX_VALUE;
                if (temp.getCpu() >= oneflavor.getCpu() && temp.getMem() >= oneflavor.getMem()) {
                    int leave = optimization == 0 ? temp.getCpu() - oneflavor.getCpu() : temp.getMem() - oneflavor.getMem();
                    if (leave < minLeave) {
                        minLeave = leave;
                        minIndex = j;
                    }
                }
            }
            // 未找到满足虚拟机资源的物理服务器
            if (minIndex == -1) {
                servers.add(new Server(String.valueOf(serverId++), physical.getCpu(), physical.getMem()));
                i--;
            } else {
                if (result.keySet().contains(minIndex)) {
                    result.get(minIndex).put(oneflavor, result.get(minIndex).getOrDefault(oneflavor, 0) + 1);
                } else {
                    HashMap<Server, Integer> add = new HashMap<>();
                    add.put(oneflavor, 1);
                    result.put(minIndex, add);
                }
                servers.get(minIndex).setCpu(servers.get(minIndex).getCpu() - oneflavor.getCpu());
                servers.get(minIndex).setMem(servers.get(minIndex).getMem() - oneflavor.getMem());
            }
        }
        List<Map<Server, Integer>> re = new ArrayList<>();
        for (int index : result.keySet()) {
            re.add(result.get(index));
        }
        return re;
    }
}
