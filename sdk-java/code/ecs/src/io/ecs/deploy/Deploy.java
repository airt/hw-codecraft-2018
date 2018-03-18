package io.ecs.deploy;

import com.elasticcloudservice.predict.Server;

import java.util.List;

public interface Deploy {

  List<List<Server>> deploy(List<Server> predictFlavors, Server physical, int optimization);

}
