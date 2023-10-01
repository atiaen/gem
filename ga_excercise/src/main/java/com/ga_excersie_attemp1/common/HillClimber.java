package com.ga_excersie_attemp1.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ga_excersie_attemp1.App;

public class HillClimber {

    String hillClimberType = "";
    String target = "";
    String currentState = "";
    float targetScore = 0f;

    String ALLCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-[]{}|;’:\",./<>?"
            + " ";

    public HillClimber(String type, String target) {
        // System.out.println(target);
        // System.out.println(type);
        this.hillClimberType = type;
        this.targetScore = target.length() * 0.1f;
        this.target = target;
    }

    public String doHillClimibing() {

        if (this.hillClimberType.equals("StringBased")) {
            this.setInitialState();

            while (!currentState.equals(target)) {

                int evalCur = this.evaluateCurrentState();

                System.out.println("Current State:   " + currentState);
                // System.out.println("Target State: " + target);

                if (currentState.equals(target)) {
                    System.out.println("Acheived Target:   " + currentState);
                    break;
                }

                if (evalCur == 1) {
                    System.out.println("Acheived Target:   " + currentState);
                    break;
                }

                String newS = this.generateNewState(currentState);
                currentState = newS;
            }

        }
        return currentState;
    }

    public void setInitialState() {

        Integer targetLength = target.length();
        this.currentState = utils.generateRandomString(targetLength);
        // System.out.println(currentState);

    }

    public int evaluateCurrentState() {

        float stateScore = 0f;
        System.out.println("Target Score: " + targetScore);

        for (int i = 0; i < currentState.length(); i++) {
            if (currentState.charAt(i) == target.charAt(i)) {
                stateScore += 0.1f;
            }
        }

        System.out.println("Current Score: " + stateScore);

        if (stateScore < targetScore) {
            return 0;
        }

        if (stateScore == targetScore) {
            return 1;
        }

        return -1;
    }

    public int evaluateStringState(String state) {

        float stateScore = 0f;

        for (int i = 0; i < state.length(); i++) {
            if (state.charAt(i) == state.charAt(i)) {
                stateScore += 0.1f;
            }
        }

        System.out.println("Current Score: " + stateScore);

        if (stateScore < targetScore) {
            return 0;
        }

        else if (stateScore >= targetScore) {
            return 1;
        }

        return -1;
    }

    public int evaluateCurrentStateAgainstPrevState(String currState, String prevState) {

        float currScore = 0f;
        float prevScore = 0f;

        for (int i = 0; i < currState.length(); i++) {
            if (currState.charAt(i) == target.charAt(i)) {
                currScore += 0.1f;
            }
        }

        for (int i = 0; i < prevState.length(); i++) {
            if (prevState.charAt(i) == target.charAt(i)) {
                prevScore += 0.1f;
            }
        }

        if (currScore < prevScore) {
            return 0;
        } else {
            return 1;
        }
    }

    public String generateNewState(String prevState) {

        String newState = "";
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
