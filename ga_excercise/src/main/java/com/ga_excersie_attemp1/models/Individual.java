package com.ga_excersie_attemp1.models;

import com.ga_excersie_attemp1.App;

import lombok.Data;

@Data
public class Individual {
    Integer individualType; // 0 for number,1 for string
    Integer individualValue = null;
    String individualString = "";
    Float individualFitness = 0f;

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
        
        // return ("Individual Value:" + this.individualValue +
        //         " Individual Type: " + this.individualType +
        //         " Individual String: " + this.individualString +
        //         " Individual Fitness: " + this.individualFitness);
    }


    public Float getInFitness(){
        char[] list = individualString.toCharArray();

        this.individualFitness = 0f;

        for(int i= 0;i < list.length; i++){
            if(list[i] == App.target.charAt(i)){
                this.individualFitness = this.individualFitness + 0.1f;
            }
        }
        return individualFitness;
    }
}
