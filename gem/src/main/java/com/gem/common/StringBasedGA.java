package com.gem.common;

import com.gem.App;
import com.gem.models.Individual;
import com.gem.models.Population;

public class StringBasedGA {
    String target;
    Integer populationSize;
    Float evalScore;
    Integer maxGenerations;
    Float crossOverRate;
    Float mutationRate;
    Float targetScore;

    public StringBasedGA(String target, Integer populationSize, Float evalScore, Integer maxGen, Float cross, Float mut,
            Float targetScore) {
        this.crossOverRate = cross;
        this.target = target;
        this.targetScore = targetScore;
        this.evalScore = evalScore;
        this.populationSize = populationSize;
        this.maxGenerations = maxGen;
        this.mutationRate = mut;
    }

    public void runGA() {

        // Generate population here
        Population p = utils.generateInitialPopulation();

        Integer generationCount = 0;

        // Find both fittest and best fit individual in current population
        Individual fittestIndividualInCurrentGen = p.findFittestIndividual();
        Individual mostFitIndividual = p.findFittestIndividual();

        // Loop while our current generation is less than max generations
        while (generationCount <= maxGenerations - 1) {
            generationCount++;

            fittestIndividualInCurrentGen = p.findFittestIndividual();
            if (fittestIndividualInCurrentGen.getInFitness() > mostFitIndividual.getInFitness()) {
                mostFitIndividual = fittestIndividualInCurrentGen;
            }

            System.out.println("Target:               " + App.target.toString());
            System.out.println("Best Individual:      " + mostFitIndividual.getIndividualString());
            System.out.println("Current Generation:      " + generationCount);

            // If we find the target stop loop and print out here
            if (fittestIndividualInCurrentGen.getIndividualString().equals(App.target)) {
                System.out.println("Hit the Target!!");
                System.out.println("Target:               " + App.target.toString());
                System.out.println("Best Individual:      " + mostFitIndividual.getIndividualString());
                System.out.println("Generation Found:      " + generationCount);
                break;
            }

            // If we reach max generation, print missed target and other info
            if (generationCount.equals(maxGenerations)) {
                System.out.println("Missed the target...");
                System.out.println("Target:                " + App.target);
                System.out.println("Best Individual:       " + mostFitIndividual.getIndividualString());
                System.out.println("Generation Found:     " + generationCount);
                System.out.println("Score: " + mostFitIndividual.getIndividualFitness());
            }

            // If above conditions fail. Generate another population based on previous
            // population and set new pop to current pop.
            Population offsPopulation = utils.createNewGeneration(p);
            p = offsPopulation;

        }

    }

}
