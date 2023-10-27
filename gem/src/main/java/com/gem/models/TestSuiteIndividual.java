package com.gem.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class TestSuiteIndividual {
    List<TestCase> testcases = new ArrayList<>();
    // String suiteId = "";
    Float fitnessScore = 0f;

    @Override
    public String toString() {
        String printOut = "[";
        for (int i = 0; i < testcases.size(); i++) {

            printOut += testcases.get(i).toString();
        }

        printOut += "] | Fitness Score: " + fitnessScore + " \n";

        return printOut;
    }

    public Float calculateIndividualAPFD() {
        Float val = 0f;

        Float tfScore = 0f;

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < testcases.size(); j++) {

                TestCase testCase = testcases.get(j);

                // String tcId = testCase.getTestCaseId();

                List<String> faultsList = testCase.getFaultsDetected();

                if (faultsList.get(i).equals("1")) {
                    tfScore += (j + 1);
                    // System.out
                    // .println("Found fault " + (i + 1) + " with Test Case " + tcId + " at
                    // position: " + (j + 1));
                    break;
                } else {
                    continue;
                }

            }
        }


        // System.out.println(testcases);

        Integer faultsCount = testcases.get(0).getFaultsDetected().size();
        Integer faultsCountMultByTestCases = faultsCount * testcases.size();

        Float firstHalf = tfScore / faultsCountMultByTestCases;
        Float secondHalf1 = 2f * testcases.size();
        Float secondHalf2 = 1 / secondHalf1;

        val = 1 - firstHalf + secondHalf2;

        fitnessScore = val;

        return val;

    }

}
