package com.shaffersoft.kata;

/**
 * Created by Joel on 1/24/16.
 */
public interface CoinValidator {

    public static final int NICKEL_WIEGHT = 5;
    public static final int NICKEL_DIAMTER = 5;
    public static final Double NICKEL_VALUE = Double.valueOf("0.05");

    public static final int DIME_WIEGHT = 10;
    public static final int DIME_DIAMTER = 10;
    public static final Double DIME_VALUE = Double.valueOf("0.10");

    public static final int QUARTER_WIEGHT = 25;
    public static final int QUARTER_DIAMTER = 25;
    public static final Double QUARTER_VALUE = Double.valueOf("0.25");


    Double getCoinValue(Coin coin) throws IllegalCoinException;
}
