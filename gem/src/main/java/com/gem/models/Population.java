package com.gem.models;

import java.util.ArrayList;
import java.util.List;

import com.gem.App;

import lombok.Data;

@Data
public class Population {

    //Array to track how many individuals exist in a given population
    public List<StringIndividual> Individuals = new ArrayList<StringIndividual>();
    
    public Population(List<StringIndividual> inds){
        this.Individuals = inds;
    }

    //Function to find the best individual in a population by 
    //checking each individuals fitness and returning the best individual
    public StringIndividual findFittestIndividual() {

        StringIndividual fittest = new StringIndividual();

        Float highestScore = 0f;

        for (StringIndividual ind : this.Individuals) {

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
