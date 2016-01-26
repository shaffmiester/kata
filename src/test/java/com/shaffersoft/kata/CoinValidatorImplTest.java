package com.shaffersoft.kata;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Created by Joel on 1/24/16.
 */
public class CoinValidatorImplTest {

    CoinValidator coinValidator = new CoinValidatorImpl();

    @Test(expected = IllegalCoinException.class)
    public void willThrowAnIllegalCoinExceptionIfAnInvalidCoinIsInserted(){
        coinValidator.getCoinValue(new Coin(0,0));
    }

    @Test
    public void willNotThroughAnErrorIfAValidCoinIsInserted(){
        coinValidator.getCoinValue(new Coin(CoinValidator.NICKEL_DIAMTER, CoinValidator.NICKEL_WIEGHT));
    }

    @Test
    public void return5WhenTheCoinHasTheWightAndDiamterOfAANickel(){
       Double actual = coinValidator.getCoinValue(new Coin(CoinValidator.NICKEL_DIAMTER, CoinValidator.NICKEL_WIEGHT));
        assertEquals(Double.valueOf("0.05"), actual);
    }

    @Test
    public void return10WhenTheCoinHasTheWeightAndDiameterOfADime(){
        Double actual = coinValidator.getCoinValue(new Coin(CoinValidator.DIME_DIAMTER, CoinValidator.DIME_WIEGHT));
        assertEquals(Double.valueOf("0.10"), actual);
    }

    @Test(expected = IllegalCoinException.class)
    public void willThrowAnIllegalCoinExceptionIfACoinWithANickelDiamterAndAndADimeWeightIsInserted(){
        coinValidator.getCoinValue(new Coin(CoinValidator.NICKEL_DIAMTER, CoinValidator.DIME_WIEGHT));
    }

    @Test
    public void return25WhenTheCoinHasTheWeightAndDiameterOfAQuarter(){
        Double actual = coinValidator.getCoinValue(new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT));
        assertEquals(Double.valueOf("0.25"), actual);
    }




}