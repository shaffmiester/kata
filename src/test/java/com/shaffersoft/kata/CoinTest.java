package com.shaffersoft.kata;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Created by Joel on 1/24/16.
 */
public class CoinTest {

    @Test
    public void coinCanBeConstruectedWithWeightAndDiameter(){
        Coin coin = new Coin(13, 27);
        assertEquals(13, coin.getWeight());
        assertEquals(27, coin.getDiamter());
    }

}