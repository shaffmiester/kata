package com.shaffersoft.kata;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

/**
 * Created by Joel on 1/23/16.
 */
public class VendingMachine {

    private CoinValidator coinValidator;
    private CoinReturn coinReturn;
    private VendingMachineDisplayer vendingMachineDisplayer;
    private ProductDispenser productDispenser;

    private Double currentValue = new Double(0);
    private List<Coin> insertedCoins;
    private List<Product> chips = new ArrayList<>();
    private List<Product> candies = new ArrayList<>();
    private List<Product> colas = new ArrayList<>();



    public VendingMachine(VendingMachineDisplayer vendingMachineDisplayer, CoinValidator coinValidator, CoinReturn coinReturn, ProductDispenser productDispenser){
        this.vendingMachineDisplayer = vendingMachineDisplayer;
        this.coinValidator = coinValidator;
        this.coinReturn = coinReturn;
        this.productDispenser = productDispenser;

        this.vendingMachineDisplayer.updateDisplay("EXACT CHANGE ONLY");
        insertedCoins = new ArrayList<>();
    }

    public void insertCoin(Coin coin){
        try {
            currentValue += coinValidator.getCoinValue(coin);
            insertedCoins.add(coin);
            String displayText = gernerateDisplayText(currentValue);
            vendingMachineDisplayer.updateDisplay(displayText);
        } catch(IllegalCoinException e){
            coinReturn.returnCoin(coin);
        }
    }

    public void dispenseProduct(Product product){
        if(currentValue >= product.getCost()){
            productDispenser.dispenseProduct(product);
            currentValue = currentValue - product.getCost();
            if(currentValue > 0){
                coinReturn.returnMoney(currentValue);
                currentValue = new Double(0);
            }
            vendingMachineDisplayer.updateDisplay("THANK YOU");
        } else {
            vendingMachineDisplayer.updateDisplay("PRICE " + gernerateDisplayText(product.getCost()));
        }
    }

    public void checkDisplay(){
        vendingMachineDisplayer.updateDisplay("INSERT COIN");
    }

    public Double getCurrentValue(){
        return currentValue;
    }

    public List<Coin> returnCoins(){
        List<Coin> coinsToReturn = new ArrayList(insertedCoins);
        insertedCoins.clear();
        vendingMachineDisplayer.updateDisplay("INSERT COIN");
        return coinsToReturn;
    }

    public int getChipsCount(){
        return chips.size();
    }

    public int getCandiesCount(){
        return candies.size();
    }

    public int getColasCount(){
        return colas.size();
    }

    public void addProduct(List<Product> products){
        for(Product product : products) {
            switch (product) {
                case CHIPS:
                    chips.add(product);
                    break;
                case CANDY:
                    candies.add(product);
                    break;
                case COLA:
                    colas.add(product);
                    break;
            }
        }
    }

    static String gernerateDisplayText(Double value) {

        Double currencyAmount = new Double(value);
        Currency currentCurrency = Currency.getInstance(Locale.US);
        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(Locale.US);

        return currencyFormatter.format(currencyAmount);
    }
}
