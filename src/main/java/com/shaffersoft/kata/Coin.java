package com.shaffersoft.kata;

/**
 * Created by Joel on 1/24/16.
 */
public class Coin {

    private int weight;
    private int diamter;

    public Coin(int weight, int diameter){
        this.weight = weight;
        this.diamter = diameter;
    }

    public int getWeight(){
        return weight;
    }

    public int getDiamter(){
        return diamter;
    }


}
