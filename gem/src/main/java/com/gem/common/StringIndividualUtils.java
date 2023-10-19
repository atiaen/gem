package com.gem.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gem.App;
import com.gem.models.StringIndividual;
import com.gem.models.Population;

public class StringIndividualUtils {

    //Tournament selection to pit multiple individuals against each other
    public static StringIndividual tournamentSelection(List<StringIndividual> individuals) {

        StringIndividual winningIndividual;
        Random randGen = new Random();

        //Select two random indexes from list
        Integer parent1Index = randGen.nextInt(individuals.size() - 1);
        Integer parent2Index = randGen.nextInt(individuals.size() - 1);

        //If both indexes are the same increase by 1
        if (parent1Index == parent2Index) {
            parent2Index += 1;
        }

        //Get the parents by index
        StringIndividual parent1 = individuals.get(parent1Index);
        StringIndividual parent2 = individuals.get(parent2Index);

        //Compare parent 1 against 2
        if (parent1.getInFitness() > parent2.getInFitness()) {
            winningIndividual = parent1;
        } else {
            winningIndividual = parent2;
        }

        //If one is better than the other return that individual
        return winningIndividual;

    }

    //Generate a new population here
    public static Population createNewGeneration(Population oldpop) {
        //Initialize list to be based on population size
        List<StringIndividual> individuals = new ArrayList<StringIndividual>(App.populationSize);

        //Increment loop by 2 
        for (int i = 0; i < App.populationSize; i += 2) {
            StringIndividual in1;
            StringIndividual in2;

            //Choose two individuals by tournament selection
            in1 = tournamentSelection(oldpop.Individuals);
            in2 = tournamentSelection(oldpop.Individuals);

            //If either parent is the same as the other,reselect another individual
            if (in1.equals(in2) || in2.equals(in1)) {
                in2 = tournamentSelection(oldpop.Individuals);
            }

            //Generate 2 offspring from parents
            StringIndividual[] generatedOffspring = generateOffSpring(in1, in2);
            StringIndividual child1 = generatedOffspring[0];
            StringIndividual child2 = generatedOffspring[1];

            //Add offspring to list
            individuals.add(i, child1);
            individuals.add(i + 1, child2);

        }

        //Return new population
        Population newPopulation = new Population(individuals);

        return newPopulation;
    }


    //Generate a random inital population
    public static Population generateInitialPopulation() {

        List<StringIndividual> individuals = new ArrayList<StringIndividual>();

        String target = App.target;
        Integer populationSize = App.populationSize;

        Integer targetLength = target.length();

        for (int i = 0; i < populationSize; i++) {
            StringIndividual newIndividual = new StringIndividual();
            newIndividual.setIndividualType(1);

            String newString = generateRandomString(targetLength);
            newIndividual.setIndividualString(newString);
            // for (int j = 0; j < targetLength; j++) {
            //     int indexOf = randomGenerator.nextInt(ALLCHARS.length());
            //     Character character = ALLCHARS.charAt(indexOf);
            //     newIndividual.setIndividualString(newIndividual.getIndividualString() + character);
            // }

            // System.out.print(newIndividual.individualString+", ");
            individuals.add(newIndividual);
        }

        Population newPopulation = new Population(individuals);
        // newPopulation.Individuals = individuals;

        return newPopulation;

    }


    //Take two parents and perfom a cross over
    public static StringIndividual[] generateOffSpring(StringIndividual p1, StringIndividual p2) {
        StringIndividual[] children = new StringIndividual[2];

        // crossover occurs here
        Random rand = new Random();
        float randProb = rand.nextFloat();

        // Check if randomly generated value is less than or equal to our crossover rate
        if (randProb <= App.crossOverRate) {
            int randInt = rand.nextInt(p1.getIndividualString().length());

            String p1Str = p1.getIndividualString();
            String p2Str = p2.getIndividualString();

            //Crossover of child strings occurs here
            String child1Str = p1Str.substring(0, randInt) + p2Str.substring(randInt);
            String child2Str = p2Str.substring(0, randInt) + p1Str.substring(randInt);

            //Instantiate 2 new children here
            StringIndividual child1 = new StringIndividual();
            child1.setIndividualString(child1Str);
            child1.setIndividualType(1);

            StringIndividual child2 = new StringIndividual();
            child2.setIndividualString(child2Str);
            child2.setIndividualType(1);

            //Mutation occurs here
            Random mutateGen = new Random();
            float probToMutate = mutateGen.nextFloat();


            //Check if probabilty to mutate is less than or equal to our mutation rate
            if (probToMutate <= App.mutationRate) {

                //Generate a random number between 1 and 2 
                int chanceToMutateChildren = generateRandomNumberWithinRange(1, 3);

                //Choose which child to mutate.
                if (chanceToMutateChildren == 1) {
                    child1 = mutateIndividual(child1);
                  
                }else if (chanceToMutateChildren == 2) {
                    child2 = mutateIndividual(child2);
                   
                }

            }

            children[0] = child1;
            children[1] = child2;

        } else {
            children[0] = p1;
            children[1] = p2;
        }
        return children;
    }

    //Mutation happens here
    public static StringIndividual mutateIndividual(StringIndividual in) {
        String ALLCHARS = returnAllCharacters();
        String individualString = in.getIndividualString();

        for (int i = 0; i < individualString.length(); i++) {
            char oldChar = individualString.charAt(i);

            Random randGen = new Random();
            int index = randGen.nextInt(ALLCHARS.length() - 1);
            char newChar = ALLCHARS.charAt(index);

            if (oldChar == newChar) {
                index = index + 1;
                newChar = ALLCHARS.charAt(index);
            }
            String newCharString = String.valueOf(newChar);
            individualString = individualString.substring(0, i) + newCharString
                    + individualString.substring(i + 1, individualString.length());

        }

        StringIndividual newIndividual = new StringIndividual();
        newIndividual.setIndividualString(individualString);
        newIndividual.setIndividualType(1);
        newIndividual.setIndividualFitness(0f);

        return newIndividual;

    }


    //Helper functions below
    public static int generateRandomNumberWithinRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static String returnAllCharacters(){
        String ALLCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-[]{}|;’:\",./<>?"
        + " ";
        return ALLCHARS;
    }

    public static String generateRandomString(Integer length){
        // Integer targetLength = target.length();
        Random randomGenerator = new Random();
        String generated = "";

        String all = returnAllCharacters();

        for (int j = 0; j < length; j++) {
            int indexOf = randomGenerator.nextInt(all.length());
            Character character = all.charAt(indexOf);
            generated = generated + character;
        }

        return generated;
    }
}
