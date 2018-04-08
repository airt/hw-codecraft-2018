package io.ecs.deploy;

import com.elasticcloudservice.predict.Server;

import java.util.List;
import java.util.Map;

public interface Deploy {

  List<Map<Server, Integer>> deploy(List<Server> predictFlavors, Server physical, int optimization);

}
