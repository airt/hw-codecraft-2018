package io.ecs.deploy;

import com.elasticcloudservice.predict.Flavor;
import com.elasticcloudservice.predict.Server;

import java.util.*;

/**
 * 虚拟机部署：模拟退火算法
 */
public class DeployWithSA implements Deploy{

    @Override
    public List<Server> deploy(List<Flavor> predictFlavors, Server server, int optimization) {
        return putFlavorToServers(predictFlavors, server.totalCpu, server.totalMem, optimization);
    }

    public List<Server> putFlavorToServers(List<Flavor> predictFlavors, int serverCpu, int serverMem, int optimization) {
        double min_score = predictFlavors.size() + 1;
        // 存放最好结果
        List<Server>  results = new ArrayList<>();
        double T = 100.0;
        double Tmin = 1;
        double r = 0.9999;
        List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < predictFlavors.size(); i++) {
            dice.add(i);
        }
        List<Server> servers = new ArrayList<>();
        while (T > Tmin) {
            Collections.shuffle(dice);
            Collections.swap(predictFlavors, dice.get(0), dice.get(1));

            servers.clear();
            Server server = new Server(serverCpu, serverMem);
            servers.add(server);

            // 虚拟机放置
            for (Flavor ele : predictFlavors) {
                int i = 0;
                for (; i < servers.size(); i++) {
                    if (servers.get(i).putFlavor(ele)) {
                        break;
                    }
                }
                if (i == servers.size()) {
                    Server newServer = new Server(serverCpu, serverMem);
                    newServer.putFlavor(ele);
                    servers.add(newServer);
                }
            }

            // 计算本次放置虚拟机的评价分数
            double serverScore = 0;
            if (optimization == 0) {
                serverScore = servers.size() - 1 + servers.get(servers.size() - 1).getCpuUsageRate();
            } else if (optimization == 1) {
                serverScore = servers.size() - 1 + servers.get(servers.size() - 1).getMemUsageRate();
            }

            Random random = new Random();
            if (serverScore < min_score) {
                min_score = serverScore;
                results.clear();
                for (int i = 0; i < servers.size(); i++) {
                    results.add(servers.get(i));
                }
            }
            // 分数更高，一定概率保存结果，放置优化陷入局部最优
            else {
                if (Math.exp((min_score - serverScore) / T) > random.nextDouble()) {
                    min_score = serverScore;
                    results.clear();
                    for (int i = 0; i < servers.size(); i++) {
                        results.add(servers.get(i));
                    }
                }
            }
            T = r * T;
        }
        return results;
    }

}
