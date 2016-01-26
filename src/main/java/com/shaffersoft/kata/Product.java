package com.shaffersoft.kata;

/**
 * Created by Joel on 1/24/16.
 */
public enum Product {

    CHIPS(Double.valueOf("0.50")), CANDY(Double.valueOf("0.65")), COLA(Double.valueOf("1.00"));

    private Double cost;

    Product(Double cost) {
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }


}
