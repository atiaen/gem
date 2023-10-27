package com.gem.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
            //Set our initial climbing state
            this.setInitialState();

            //Score inital state
            Float bestScore = this.scoreString(currentState);
            
            String bestState = currentState;
            Integer count = 0;

            //While target has not been reached
            while (true) {
                count++;
                System.out.println("Current State:   " + currentState);
                System.out.println("Current Count:   " + count);

                //Stopping condition, checking if our target has been reached
                if (currentState.equals(target)) {
                    System.out.println("Acheived Target:   " + currentState);
                    break;
                }

                //Generate new neighbours based on current position/state
                List<String> newNeighbors = this.generateNeighbors(currentState);
                //Find the best neighour index from the generated neighbourhood
                Integer bestNew = this.findBestNeighbourInNeighborhood(newNeighbors);
                //Get that neighbour
                String newS = newNeighbors.get(bestNew);
                System.out.println("New State:   " + newS);

                //Check if current state is the same as new state. if true use next index
                if (newS.equals(currentState)) {
                    newS = newNeighbors.get(bestNew + 1);
                }

                //Score our new state
                float newScore = this.scoreString(newS);

                System.out.println("Best State:   " + bestState);
                System.out.println("Best Score:   " + bestScore);
                System.out.println("New Score:   " + newScore);

                //If our new score is better than best score 
                //Set current state and best score to that values if not everything will repeat again
                if (newScore < bestScore) {
                    bestScore = newScore;
                    currentState = newS;
                    bestState = newS;
                }
            }

        }
    }

    public void setInitialState() {

        //Get length of our target string
        Integer targetLength = target.length();

        //Generate a random string and set inital state to that string
        this.currentState = utils.generateRandomString(targetLength);

    }

    //Fitness function to score a string. Is done character by character
    public Float scoreString(String s) {
        float score = targetScore;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == target.charAt(i)) {
                score -= 0.1f;
            }
        }

        return score;
    }


    //Create a list of neighbours from out current position
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

    //Take a list of neighours and score each one to find the best neighbour by index
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
