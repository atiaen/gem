package com.gem.models;

import java.util.ArrayList;
import java.util.List;

import com.gem.App;

import lombok.Data;

@Data
public class Population {

    //Array to track how many individuals exist in a given population
    public List<Individual> Individuals = new ArrayList<Individual>();
    
    public Population(List<Individual> inds){
        this.Individuals = inds;
    }

    //Function to find the best individual in a population by 
    //checking each individuals fitness and returning the best individual
    public Individual findFittestIndividual() {

        Individual fittest = new Individual();

        Float highestScore = 0f;

        for (Individual ind : this.Individuals) {

            // System.out.println(ind);
            Float indScore = ind.getInFitness();
            if (indScore >= App.targetScore || indScore > highestScore) {
                highestScore = indScore;
                fittest = ind;
            }

        }

        return fittest;
    }

}
