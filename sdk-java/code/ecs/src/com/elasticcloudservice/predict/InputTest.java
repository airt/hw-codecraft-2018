package com.elasticcloudservice.predict;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Input Test
 */
class InputTest {

    Input input = null;

    @BeforeEach
    void setUp() {
        String[] inputContent = new String[13];
        inputContent[0] = "56 128 1200";
        inputContent[1] = "";
        inputContent[2] = "5";
        inputContent[3] = "flavor1 1 1024";
        inputContent[4] = "flavor2 1 2048";
        inputContent[5] = "flavor3 1 4096";
        inputContent[6] = "flavor4 2 2048";
        inputContent[7] = "flavor5 2 4096";
        inputContent[8] = "";
        inputContent[9] = "CPU";
        inputContent[10] = "";
        inputContent[11] = "2015-02-20 00:00:00";
        inputContent[12] = "2015-02-27 00:00:00";

        input = new Input(inputContent);
    }

    @Test
    void getPhysical() {
        assertEquals(56, input.getPhysical().getCpu());
        assertEquals(128, input.getPhysical().getMem());
    }

    @Test
    void getFlavors() {
        List<Server> flavors = input.getFlavors();
        assertEquals(5, flavors.size());
        assertEquals(4, flavors.get(2).getMem());
        assertEquals(2, flavors.get(4).getCpu());
    }

    @Test
    void getOptimization() {
        assertEquals(0, input.getOptimization());
    }

    @Test
    void getStartTime() {
        assertEquals("2015-02-20 00:00:00", input.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals("2015-02-27 00:00:00", input.getEndTime());
    }

}