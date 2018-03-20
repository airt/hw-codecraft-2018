package io.ecs.generator;

import com.elasticcloudservice.predict.Record;
import com.elasticcloudservice.predict.Server;
import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;

import java.util.List;
import java.util.Map;

public interface DataGenerator {

    Map<String, Tuple2<Matrix, Matrix>> generate(List<Record> records, List<Server> flavors, int T);

}
