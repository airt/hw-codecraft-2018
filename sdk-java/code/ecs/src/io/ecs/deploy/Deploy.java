package io.ecs.deploy;

import com.elasticcloudservice.predict.Flavor;
import com.elasticcloudservice.predict.Server;

import java.util.List;

public interface Deploy {

  List<Server> deploy(List<Flavor> predictFlavors, Server server, int optimization);

}
