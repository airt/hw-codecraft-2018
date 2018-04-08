package io.ecs.deploy;

import com.elasticcloudservice.predict.Server;
import com.filetool.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pinker on 2018/3/30
 */
public class DeployWithDP implements Deploy {

  /**
   * @param predictFlavors 预测的虚拟机列表
   * @param physical       服务器信息
   * @param optimization
   * @return
   */
  @Override
  public List<Map<Server, Integer>> deploy(List<Server> predictFlavors, Server physical, int optimization) {
    Map<Server, Integer> VMServers = new HashMap<>();
    for (Server server : predictFlavors)
      VMServers.merge(server, 1, (a, b) -> a + b);
    for (Map.Entry<Server,Integer> vms: VMServers.entrySet()) {
      System.out.println(vms.getKey()+"\t"+vms.getValue());
    }


    return null;
  }

  /**
   * 在物理服务器上进行虚拟机资源分配
   * 算法采用：首次适应算法 （后续可优化为降序最佳适应法）
   * 还要考虑单个维度资源问题：比如维度为CPU，那么在最佳适应法中选择满足情况中CPU最满的情况
   *
   * @param predictFlavors
   * @param physical
   * @param optimization
   * @return
   */
  private static Map<Integer, Map<Server, Integer>> deploy(Map<Server, Integer> predictFlavors, Server physical, int optimization) {
    LogUtil.printLog("physical cpu:" + physical.getCpu());
    List<Server> servers = new ArrayList<Server>();
    Map<Integer, Map<Server, Integer>> result = new HashMap<>();
    int serverId = 1;
    servers.add(new Server(String.valueOf(serverId++), physical.getCpu(), physical.getMem()));
    for (Server flavor : predictFlavors.keySet()) {
      int num = predictFlavors.get(flavor);
      for (int i = 0; i < num; i++) {
        boolean flag = false;
        for (int j = 0; j < servers.size(); j++) {
          Server temp = servers.get(j);
          //可以放下这个虚拟机
          if (temp.getCpu() >= flavor.getCpu() && temp.getMem() >= flavor.getMem()) {
            if (result.keySet().contains(j)) {
              result.get(j).put(flavor, result.get(j).getOrDefault(flavor, 0) + 1);
            } else {
              HashMap<Server, Integer> add = new HashMap<>();
              add.put(flavor, 1);
              result.put(j, add);
            }
            temp.setCpu(temp.getCpu() - flavor.getCpu());
            temp.setMem(temp.getMem() - flavor.getMem());
            flag = true;
            break;
          }
        }
        if (!flag) {
          servers.add(new Server(String.valueOf(serverId++), physical.getCpu(), physical.getMem()));
          i--;
        }
      }
    }
    return result;
  }

}
