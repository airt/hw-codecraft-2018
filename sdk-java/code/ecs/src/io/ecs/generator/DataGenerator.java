package io.ecs.generator;

import com.elasticcloudservice.predict.Record;
import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;

public interface DataGenerator {

  Tuple2<Matrix, Matrix> generate(Iterable<Record> records);

}
