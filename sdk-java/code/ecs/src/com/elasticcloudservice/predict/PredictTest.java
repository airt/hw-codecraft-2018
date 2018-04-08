package com.elasticcloudservice.predict;

import com.filetool.util.Utils;
import io.ecs.common.ColVector;
import io.ecs.common.Matrix;
import io.ecs.common.RowVector;
import io.ecs.common.Tuple2;
import io.ecs.model.LinearRegression;
import io.ecs.model.Model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Predict Test
 */
public class PredictTest {

    @Test
    void generateHistory() {
        String[] ecsContent = new String[3];
        ecsContent[0] = "56498c50-84e4\tflavor15\t2015-01-01 19:03:32";
        ecsContent[1] = "56498c51-8cb9\tflavor15\t2015-01-01 19:03:34";
        ecsContent[2] = "56498c52-a50e\tflavor8\t2015-01-01 23:26:04";
        List<Record> history = Predict.generateHistory(ecsContent);
        assertEquals(3, history.size());
        assertEquals("flavor15", history.get(1).getFlavorName());
        assertEquals("2015-01-01", history.get(2).getCreateTime());
    }

    @Test
    void predict() {
        // 3 * x_1 + 7
        Matrix X = Matrix.of(new double[][] {
                {1, 2},
        });
        Matrix Y = RowVector.of(
                10, 13
        );
        // 22
        Matrix predictX = ColVector.of(
                5
        );
        Model model = new LinearRegression(0.001, 100000);
        Tuple2<Tuple2<Matrix, Matrix>, Matrix> train = Tuple2.of(Tuple2.of(X, predictX), Y);
        Map<String, Tuple2<Tuple2<Matrix, Matrix>, Matrix>> data = new HashMap<>();
        data.put("flavor1", train);
        HashMap<Server, Integer> predictRe = Predict.predict(model, data);
        for (Server flavor: predictRe.keySet()) {
            System.out.println(flavor.getName() + ":" + predictRe.get(flavor));
        }
    }

    @Test
    void generateResults() {
        HashMap<Server, Integer> predictRe = new HashMap<>();
        Server flavor1 = new Server("flavor1", (int)Utils.getFlavorCpuMem(1)[0], (int)Utils.getFlavorCpuMem(1)[1]);
        Server flavor3 = new Server("flavor3", (int)Utils.getFlavorCpuMem(3)[0], (int)Utils.getFlavorCpuMem(3)[1]);
        predictRe.put(flavor1, 3);
        predictRe.put(flavor3, 5);
        List<Map<Server, Integer>> deployRe = new ArrayList<>();
        Map<Server, Integer> one = new HashMap<>();
        Map<Server, Integer> two = new HashMap<>();
        one.put(flavor1, 3);
        two.put(flavor3, 3);
        deployRe.add(one);
        deployRe.add(two);
        String[] results = Predict.generateResults(predictRe, deployRe);
        for (String ele : results) {
            System.out.println(ele);
        }
    }
}
