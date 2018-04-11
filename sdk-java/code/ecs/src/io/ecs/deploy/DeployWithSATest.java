package io.ecs.deploy;

import com.elasticcloudservice.predict.Flavor;
import com.elasticcloudservice.predict.Server;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Deploy SA Test
 */
public class DeployWithSATest {

    @Test
    void putFlavorToServers() {
        DeployWithSA deployWithSA = new DeployWithSA();
        List<Flavor> predictFlavors = new ArrayList<>();
        Flavor flavor1 = new Flavor("flavor1", 1, 1);
        Flavor flavor2 = new Flavor("flavor2", 1, 2);
        Flavor flavor3 = new Flavor("flavor3", 1, 4);
        Flavor flavor4 = new Flavor("flavor4", 2, 2);
        Flavor flavor5 = new Flavor("flavor5", 2, 4);
        for (int i = 0; i < 30; i++) {
            predictFlavors.add(flavor1);
        }
        for (int i = 0; i < 40; i++) {
            predictFlavors.add(flavor2);
        }
        for (int i = 0; i < 20; i++) {
            predictFlavors.add(flavor3);
        }
        for (int i = 0; i < 10; i++) {
            predictFlavors.add(flavor4);
        }
        for (int i = 0; i < 25; i++) {
            predictFlavors.add(flavor5);
        }
        int serverCpu = 56;
        int serverMem = 128;
        int optimization = 0;
        List<Server> servers = deployWithSA.putFlavorToServers(predictFlavors, serverCpu, serverMem, optimization);
        for (int i = 0; i < servers.size(); i++) {
            System.out.println("Server " + (i + 1) + ":");
            for (Flavor ele : servers.get(i).flavors) {
                System.out.print(ele.name + " 1 ");
            }
            System.out.println();
        }
    }
}
