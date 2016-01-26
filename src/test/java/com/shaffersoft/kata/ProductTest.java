package com.shaffersoft.kata;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Joel on 1/24/16.
 */
public class ProductTest {

    @Test
    public void costsAreAssingedCorrecly(){
        assertEquals(Double.valueOf("0.50"),Product.CHIPS.getCost());
        assertEquals(Double.valueOf("0.65"),Product.CANDY.getCost());
        assertEquals(Double.valueOf("1.00"),Product.COLA.getCost());
    }

}