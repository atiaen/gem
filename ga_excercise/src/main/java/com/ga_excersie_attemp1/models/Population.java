package com.ga_excersie_attemp1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ga_excersie_attemp1.App;

import lombok.Data;

@Data
public class Population {
    public List<Individual> Individuals = new ArrayList<Individual>();
    
    public Population(List<Individual> inds){
        this.Individuals = inds;
    }

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

    // public void evolvePopulation(){

    // }

}
