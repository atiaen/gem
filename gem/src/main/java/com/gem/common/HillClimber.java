package com.gem.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gem.models.Individual;

public class HillClimber {

    String hillClimberType = "";
    String target = "";
    String currentState = "";
    float targetScore = 0f;
    Integer neighborhoodSize = 0;

    public HillClimber(String type, String target, Integer size) {
        this.hillClimberType = type;
        this.targetScore = target.length() * 0.1f;
        this.target = target;
        this.neighborhoodSize = size;
    }

    public void doHillClimibing() {

        if (this.hillClimberType.equals("StringBased")) {
            this.setInitialState();

            Float bestScore = this.scoreString(currentState);
            String bestState = currentState;
            Integer count = 0;

            while (true) {
                count++;
                System.out.println("Current State:   " + currentState);
                System.out.println("Current Count:   " + count);
                // System.out.println("Target State: " + target);

                if (currentState.equals(target)) {
                    System.out.println("Acheived Target:   " + currentState);
                    break;
                }

                // String newS = this.generateNewState(currentState);
                List<String> newNeighbors = this.generateNeighbors(currentState);
                Integer bestNew = this.findBestNeighbourInNeighborhood(newNeighbors);
                String newS = newNeighbors.get(bestNew);
                System.out.println("New State:   " + newS);

                if (newS.equals(currentState)) {
                    newS = newNeighbors.get(bestNew + 1);
                }

                float newScore = this.scoreString(newS);

                System.out.println("Best State:   " + bestState);
                System.out.println("Best Score:   " + bestScore);
                System.out.println("New Score:   " + newScore);

                if (newScore < bestScore) {
                    bestScore = newScore;
                    currentState = newS;
                    bestState = newS;
                }
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

    public List<String> generateNeighbors(String currentNeighbor) {
        List<String> neighbors = new ArrayList<>();
        String ALLCHARS = utils.returnAllCharacters();
        Integer currentNeighborLength = currentNeighbor.length();

        for (int i = 0; i < neighborhoodSize; i++) {
            String newNeighbour = "";
            Random randGen = new Random();

            int index = randGen.nextInt(ALLCHARS.length());
            int charIndex = randGen.nextInt(currentNeighborLength);

            char newChar = ALLCHARS.charAt(index);
            char oldChar = currentNeighbor.charAt(charIndex);

            if (oldChar == newChar) {
                int index2= randGen.nextInt(ALLCHARS.length());
                newChar = ALLCHARS.charAt(index2);
            }

            String newCharString = String.valueOf(newChar);
            newNeighbour = currentNeighbor.substring(0, charIndex) + newCharString
                    + currentNeighbor.substring(charIndex + 1, currentNeighborLength);

            neighbors.add(newNeighbour);
        }

        System.out.println(neighbors);
        return neighbors;
    }

    public Integer findBestNeighbourInNeighborhood(List<String> neighbors){
        float bestScore = 0f;
        int bestNeighborIndex = 0;

        for(int i = 0; i < neighbors.size();i++){
            float neighborScore = this.scoreString(neighbors.get(i));
            if(neighborScore < bestScore){
                bestScore = neighborScore;
                bestNeighborIndex = i;
            }
        }

        return bestNeighborIndex;
    }
}
