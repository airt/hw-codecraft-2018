package com.elasticcloudservice.predict;

import io.ecs.common.Matrix;
import io.ecs.common.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DataPre Test
 */
public class DataPreTest {
    DataPre dataPre = null;

    @BeforeEach
    void setUp() {
        List<Record> history = new LinkedList<>();
        history.add(new Record("56498c51-8cb1", "flavor1", "2015-01-01"));
        history.add(new Record("56498c51-8cb2", "flavor2", "2015-01-02"));
        history.add(new Record("56498c51-8cb3", "flavor4", "2015-01-03"));
        history.add(new Record("56498c51-8cb4", "flavor4", "2015-01-03"));
        history.add(new Record("56498c51-8cb5", "flavor4", "2015-01-04"));
        dataPre = new DataPre(history);
    }

    @Test
    void nums() {
        Matrix nums = dataPre.nums();
        System.out.println(nums.show());
        assertEquals(Tuple2.of(4, 4), nums.shape());
        assertEquals(2.0, nums.get(3, 2));
    }

    @Test
    void cpus() {
        Matrix cpus = dataPre.cpus();
        System.out.println(cpus.show());
    }

    @Test
    void mems() {
        Matrix mems = dataPre.mems();
        System.out.println(mems.show());
    }
}
