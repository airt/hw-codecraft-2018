package io.ecs.deploy;

import com.elasticcloudservice.predict.Server;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 56 128 1200
 * <p>
 * 5
 * flavor1 1 1024
 * flavor2 1 2048
 * flavor3 1 4096
 * flavor4 2 2048
 * flavor5 2 4096
 * <p>
 * CPU
 * <p>
 * 2015-02-20 00:00:00
 * 2015-02-27 00:00:00
 *
 * @author pinker on 2018/3/30
 */
class DeployWithDPTest {
  private static List<Server> list;
  private DeployWithDP deploy;

  @BeforeAll
  public static void init() throws Exception {
    list = new ArrayList<Server>() {
      {
        add(new Server("flavor1", 1, 1024));
        add(new Server("flavor2", 1, 1024 * 2));
        add(new Server("flavor1", 1, 1024));
        add(new Server("flavor4", 2, 1024 * 2));
        add(new Server("flavor1", 1, 1024));
        add(new Server("flavor2", 1, 1024 * 2));
        add(new Server("flavor3", 1, 1024 * 4));
        add(new Server("flavor3", 1, 1024 * 4));
        add(new Server("flavor3", 1, 1024 * 4));
        add(new Server("flavor4", 2, 1024 * 2));
        add(new Server("flavor1", 1, 1024));
        add(new Server("flavor2", 1, 1024 * 2));
        add(new Server("flavor2", 1, 1024 * 2));
        add(new Server("flavor2", 1, 1024 * 2));
        add(new Server("flavor5", 2, 1024 * 4));
      }
    };
  }

  @BeforeEach
  public void setUp() throws Exception {
    deploy = new DeployWithDP();
  }

  @Test
  void deploy() {
    deploy.deploy(list, null, 1);
  }
}
