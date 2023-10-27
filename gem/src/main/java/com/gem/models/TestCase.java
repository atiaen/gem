package com.gem.models;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class TestCase {
    String testCaseId;

    List<String> faultsDetected;


    @Override
    public String toString() {
        return "TestCase Id:" + testCaseId + " Faults Detected: " + Arrays.toString(faultsDetected.toArray()) + " \n";
    }

}
