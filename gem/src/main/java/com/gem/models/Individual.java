package com.gem.models;

import com.gem.App;

import lombok.Data;

@Data
public class Individual {
    //What representation should be used for this individual
    Integer individualType;
    //If representation is 0 then value would be here
    Integer individualValue = null;
    //If representation is 1 then value ow
    String individualString = "";
    //Calculated fitness of the individual will be reset eventually
    Float individualFitness = 0f;

    //For logging purposes only,used to print out details of an individual
    @Override
    public String toString() {
        if(this.individualType == 0){
            return ("[ Individual Value:" + this.individualValue +
                " Individual Type: " + this.individualType +
                " Individual Fitness: " + this.individualFitness + " ] ");
        }

         return ("[ Individual String: " + this.individualString +
                " Individual Type: " + this.individualType +
                " Individual Fitness: " + this.individualFitness + " ] ");
    }


    /*
    * Fitness function calculation for each individual
    * It calculates how close an individuals string is to the target 
    * and returns a float values of that score
    */
    public Float getInFitness(){
        char[] list = individualString.toCharArray();

        this.individualFitness = 0f;

        for(int i= 0;i < list.length; i++){
            if(list[i] == App.target.charAt(i)){
                this.individualFitness = this.individualFitness + App.evalScore;
            }
        }
        return individualFitness;
    }
}
