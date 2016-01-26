package com.shaffersoft.kata;

/**
 * Created by Joel on 1/24/16.
 */
public class CoinValidatorImpl implements CoinValidator{

    @Override
    public Double getCoinValue(Coin coin){
        if(coin.getWeight() == CoinValidator.NICKEL_DIAMTER && coin.getDiamter() == CoinValidator.NICKEL_WIEGHT){
            return CoinValidator.NICKEL_VALUE;
        } else if (coin.getDiamter() == CoinValidator.DIME_DIAMTER && coin.getWeight() == CoinValidator.DIME_WIEGHT){
            return CoinValidator.DIME_VALUE;
        } else if(coin.getWeight() == CoinValidator.QUARTER_DIAMTER && coin.getDiamter() == CoinValidator.QUARTER_WIEGHT){
            return CoinValidator.QUARTER_VALUE;
        }
        else {
            throw new IllegalCoinException();
        }
    }
}
