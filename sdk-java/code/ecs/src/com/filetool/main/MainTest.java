package com.filetool.main;

import com.elasticcloudservice.predict.Server;
import com.filetool.util.Utils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Main Test
 */
public class MainTest {

    @Test
    void generateActual() {
        Server flavor1 = new Server("flavor1", (int)Utils.getFlavorCpuMem(1)[0], (int)Utils.getFlavorCpuMem(1)[1]);
        Server flavor2 = new Server("flavor2", (int)Utils.getFlavorCpuMem(2)[0], (int)Utils.getFlavorCpuMem(2)[1]);
        List<Server> flavors = new ArrayList<>();
        flavors.add(flavor1);
        flavors.add(flavor2);
        String[] testContent = new String[4];
        testContent[0] = "564bfe6d-92f2\tflavor2\t2015-05-04 09:15:58";
        testContent[1] = "564bfe6e-b8b3\tflavor2\t2015-05-04 09:15:59";
        testContent[2] = "564bfe70-8427\tflavor1\t2015-05-04 09:16:00";
        testContent[3] = "564bfe71-8798\tflavor14\t2015-05-04 17:04:29";
        Map<Server, Integer> actual = Main.generateActual(flavors, testContent);
        assertEquals(2, actual.size());
        assertEquals(1, (int)actual.get(flavor1));
        assertEquals(2, (int)actual.get(flavor2));
    }

    @Test
    void generatePredict() {
        Server flavor1 = new Server("flavor1", (int)Utils.getFlavorCpuMem(1)[0], (int)Utils.getFlavorCpuMem(1)[1]);
        Server flavor2 = new Server("flavor2", (int)Utils.getFlavorCpuMem(2)[0], (int)Utils.getFlavorCpuMem(2)[1]);
        List<Server> flavors = new ArrayList<>();
        flavors.add(flavor1);
        flavors.add(flavor2);
        String[] resultsContent = new String[5];
        resultsContent[0] = "8";
        resultsContent[1] = "flavor1 3";
        resultsContent[2] = "flavor2 5";
        resultsContent[3] = "";
        resultsContent[4] = "2";
        Map<Server, Integer> predict = Main.generatePredict(flavors, resultsContent);
        assertEquals(2, predict.size());
        assertEquals(3, (int)predict.get(flavor1));
        assertEquals(5, (int)predict.get(flavor2));
    }
}
