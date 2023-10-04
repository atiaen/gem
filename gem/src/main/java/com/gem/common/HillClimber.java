package com.gem.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HillClimber {

    String hillClimberType = "";
    String target = "";
    String currentState = "";
    float targetScore = 0f;

    public HillClimber(String type, String target) {
        // System.out.println(target);
        // System.out.println(type);
        this.hillClimberType = type;
        this.targetScore = target.length() * 0.1f;
        this.target = target;
    }

    public void doHillClimibing() {

        if (this.hillClimberType.equals("StringBased")) {
            this.setInitialState();

            Float bestScore = this.scoreString(currentState);
            String bestState = currentState;
            Integer count = 0;

            while (bestScore > 0) {
                count++;
                System.out.println("Current State:   " + currentState);
                System.out.println("Current Count:   " + count);
                // System.out.println("Target State: " + target);

                if (currentState.equals(target)) {
                    System.out.println("Acheived Target:   " + currentState);
                    break;
                }

                String newS = this.generateNewState(currentState);
                // List<String> newNeighbors = this.randomNeighbors(numberOfNeighbors);
                // Integer bestNew = this.findBestNeighborInList(newNeighbors);
                // String newS = newNeighbors.get(bestNew);
                System.out.println("New State:   " + newS);

                if (newS.equals(currentState)) {
                    newS = utils.generateRandomString(target.length());
                }

                float newScore = this.scoreString(newS);

                System.out.println("Best State:   " + bestState);
                System.out.println("Best Score:   " + bestScore);
                System.out.println("New Score:   " + newScore);

                if (newScore < bestScore) {
                    bestScore = newScore;
                    currentState = newS;
                    bestState = newS;
                } else {
                    // List<String> newNeighbors2 = this.randomNeighbors(numberOfNeighbors);
                    // Integer bestNew2 = this.findBestNeighborInList(newNeighbors);
                    // String transformed = this.generateNewState(newNeighbors2.get(bestNew2));
                    // currentState = transformed;
                    currentState = utils.generateRandomString(target.length());
                    // String newS2 = this.generateNewState(currentState);
                    // float an = this.scoreString(newS2);
                }

                // currentState = utils.generateRandomString(target.length());
            }

        }
    }

    public void setInitialState() {

        Integer targetLength = target.length();
 
        this.currentState = utils.generateRandomString(targetLength);
        // System.out.println(currentState);

    }

    public Float scoreString(String s) {
        float score = targetScore;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == target.charAt(i)) {
                score -= 0.1f;
            }
        }

        return score;
    }

    public String generateNewState(String prevState) {

        String newState = "";
        String ALLCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-[]{}|;’:\",./<>?"
                + " ";
        Integer targetLength = prevState.length();

        for (int i = 0; i < targetLength; i++) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(ALLCHARS.length());
            char chars = ALLCHARS.charAt(index);
            String va = String.valueOf(chars);
            newState = prevState.substring(0, i) + va + prevState.substring(i + 1, targetLength);
        }

        return newState;
    }


}
