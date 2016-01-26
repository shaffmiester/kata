package com.shaffersoft.kata;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by Joel on 1/23/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    @Mock
    CoinValidator mockCoinValidator;

    @Mock
    CoinReturn mockCoinReturn;

    @Mock
    VendingMachineDisplayer mockVendingMachineDisplayer;

    @Mock
    ProductDispenser mockProductDispenser;

    VendingMachine vendingMachine;

    private Coin penny = new Coin(1, 1);
    private Coin nickel = new Coin(CoinValidator.NICKEL_DIAMTER, CoinValidator.NICKEL_DIAMTER);
    private Coin dime = new Coin(CoinValidator.DIME_DIAMTER, CoinValidator.DIME_DIAMTER);
    private Coin quarter = new Coin(CoinValidator.NICKEL_DIAMTER, CoinValidator.NICKEL_DIAMTER);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        vendingMachine = new VendingMachine(mockVendingMachineDisplayer, mockCoinValidator, mockCoinReturn, mockProductDispenser);

        when(mockCoinValidator.getCoinValue(nickel)).thenReturn(CoinValidator.NICKEL_VALUE);
        when(mockCoinValidator.getCoinValue(dime)).thenReturn(CoinValidator.DIME_VALUE);

        when(mockCoinValidator.getCoinValue(quarter)).thenReturn(CoinValidator.QUARTER_VALUE);

        when(mockCoinValidator.getCoinValue(penny)).thenThrow(IllegalCoinException.class);

    }

    @Test
    public void onStartUpTheVendingMachineWillDeferToTheVendingMachineDisplayerToSetMessageToInsertExactChange(){
        verify(mockVendingMachineDisplayer, times(1)).updateDisplay("EXACT CHANGE ONLY");
    }

    @Test
    public void insertCoinDefersToCoinConverter(){
        vendingMachine.insertCoin(nickel);

        verify(mockCoinValidator, times(1)).getCoinValue(anyObject());
    }

    @Test
    public void insertCoinDefersToCoinConverterWithThePassedInCoin(){
        vendingMachine.insertCoin(nickel);

        verify(mockCoinValidator, times(1)).getCoinValue(nickel);
    }

    @Test
    public void insertCoinDoesNotThrowAnExceptionWhenAnInvalidCoinIsEntered(){
        vendingMachine.insertCoin(penny);
    }



    @Test
    public void insertCoinDoesNotThrowAnExceptionWhenAnInvalidValidCoinIsInserted(){
        vendingMachine.insertCoin(penny);
    }

    @Test
    public void insertCoinsDefersToTheCoinReturnWhenAnInvalidCoinIsInserted(){
        vendingMachine.insertCoin(penny);

        verify(mockCoinReturn, times(1)).returnCoin(anyObject());
    }

    @Test
    public void insertCoinDoesNotDeferToTheCoinReturnWhenAnValidCoinIsInserted(){
        vendingMachine.insertCoin(nickel);

        verify(mockCoinReturn, times(0)).returnCoin(anyObject());
    }

    @Test
    public void insertCoinDoesNotIncrementTheCurrentValueWhenAnInvalidCoinIsInserted(){
        vendingMachine.insertCoin(penny);

        assertEquals(Double.valueOf(0), vendingMachine.getCurrentValue());

    }

    @Test
    public void insertCoinDoesIncrementTheCurrentValueWhenAnValidCoinIsInserted(){
        vendingMachine.insertCoin(nickel);

        assertEquals(CoinValidator.NICKEL_VALUE, vendingMachine.getCurrentValue());
    }


    @Test
    public void insertCoinDoesUpdateTheDisplayWithTheNewCurrentValue(){
        vendingMachine.insertCoin(nickel);

        verify(mockVendingMachineDisplayer).updateDisplay("$0.05");
    }

    @Test
    public void if50CentsIsEnteredAndChipsIsSelectedTheVendingMachineWillDeferToTheProductDisplenserToDispenseTheProduct(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockProductDispenser, times(1)).dispenseProduct(anyObject());
    }

    @Test
    public void if65CentsIsEnteredCandyCanBeDispensed(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(dime);
        vendingMachine.insertCoin(nickel);


        vendingMachine.dispenseProduct(Product.CANDY);

        verify(mockProductDispenser, times(1)).dispenseProduct(anyObject());
    }

    @Test
    public void ifADollarCentsIsEnteredColaCanBeDispensed(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(dime);
        vendingMachine.insertCoin(dime);
        vendingMachine.insertCoin(nickel);



        vendingMachine.dispenseProduct(Product.COLA);

        verify(mockProductDispenser, times(1)).dispenseProduct(anyObject());
    }

    @Test
    public void if50CentsIsEnteredAndChipsIsSelectedTheCurrentValueIsResetTo0(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);

        vendingMachine.dispenseProduct(Product.CHIPS);

        assertEquals(Double.valueOf("0"), vendingMachine.getCurrentValue());
    }

    @Test
    public void ifAProductHasBeenDispensedThenThankYouMessageIsSentToTheDisplay(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);

        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockVendingMachineDisplayer).updateDisplay("THANK YOU");
    }

    @Test
    public void ifAProductHasBeenDispensedWhenTheUserChecksTheDisplayAgainItWillSayInsertCoins(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);

        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockVendingMachineDisplayer).updateDisplay("THANK YOU");

        vendingMachine.checkDisplay();

        verify(mockVendingMachineDisplayer, times(1)).updateDisplay("INSERT COIN");

    }

    @Test
    public void ifAProductHasSelectedButNotEnoughMoneyHasBeenInsertedAnAppopriateMessageIsDisplayed(){
        vendingMachine.insertCoin(quarter);

        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockVendingMachineDisplayer).updateDisplay("PRICE $0.50");

        vendingMachine.dispenseProduct(Product.CANDY);

        verify(mockVendingMachineDisplayer).updateDisplay("PRICE $0.65");

        vendingMachine.dispenseProduct(Product.COLA);

        verify(mockVendingMachineDisplayer).updateDisplay("PRICE $1.00");
    }


    @Test
    public void whenAProductIsSelectedButNoCoinsHaveBeenInserterdTheVendingMachineWillNotDeferToTheProductDisplenser(){
        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockProductDispenser, times(0)).dispenseProduct(anyObject());
    }

    @Test
    public void ifMoneyHasBeenInsertedAndProductIssIsSelectedButNotEnoughtMoneyHasBeenSelectedThenNoProductWillBeDispensed(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockProductDispenser, times(0)).dispenseProduct(anyObject());
    }

    @Test
    public void ifMoreMoneyHasBeenInsertedThanTheSelectedProductCostsChangeIsReturned(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(dime);
        vendingMachine.insertCoin(nickel);

        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockCoinReturn, times(1)).returnMoney(anyObject());


    }

    @Test
    public void ifMoreMoneyHasBeenInsertedThanTheSelectedProductCostsTheCurrentAmountIsStillResetToZero(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(dime);
        vendingMachine.insertCoin(nickel);

        vendingMachine.dispenseProduct(Product.CHIPS);

        assertEquals(Double.valueOf("0.00"), vendingMachine.getCurrentValue());

    }

    @Test
    public void ifExactChangeIsEnteredNoChangeIsReturned(){
        vendingMachine.insertCoin(quarter);
        vendingMachine.insertCoin(quarter);

        vendingMachine.dispenseProduct(Product.CHIPS);

        verify(mockCoinReturn, times(0)).returnMoney(anyObject());


    }

    @Test
    public void whenReturnCoinsIsSelectedAndOneCoinHasBeenInsertedThatCoinIsReturned(){
        Coin quarterOne = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);

        vendingMachine.insertCoin(quarterOne);

        List<Coin> returnedCoins = vendingMachine.returnCoins();

        assertSame(quarterOne, returnedCoins.get(0));


    }

    @Test
    public void whenACoinIsEnteredAndReturnCoinsIsSelectedAndReturnCoinIsPressedAgainNullIsReturned(){
        Coin quarterOne = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);

        vendingMachine.insertCoin(quarterOne);
        vendingMachine.returnCoins();

        List<Coin> actual = vendingMachine.returnCoins();

        assertNotNull(actual);
        assertEquals(0,actual.size());

    }

    @Test
    public void whenTwoCoinsAreInsertedAndReturnCoinsIsPressedBothCoinsAreReturned(){
        Coin quarterOne = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);
        Coin dimeOne = new Coin(CoinValidator.DIME_WIEGHT, CoinValidator.DIME_DIAMTER);

        vendingMachine.insertCoin(quarterOne);
        vendingMachine.insertCoin(dimeOne);


        List<Coin> returnedCoins = vendingMachine.returnCoins();

        assertNotNull(returnedCoins);
        assertSame(quarterOne, returnedCoins.get(0));
        assertSame(dimeOne, returnedCoins.get(1));


    }

    @Test
    public void whenMultipleCoinsAreInsertedAndReturnCoinsIsPressedAllCoinsAreReturned(){
        Coin quarterOne = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);
        Coin quarterTwo = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);
        Coin dimeOne = new Coin(CoinValidator.DIME_WIEGHT, CoinValidator.DIME_DIAMTER);
        Coin nickelOne = new Coin(CoinValidator.NICKEL_WIEGHT, CoinValidator.NICKEL_DIAMTER);


        vendingMachine.insertCoin(quarterOne);
        vendingMachine.insertCoin(dimeOne);
        vendingMachine.insertCoin(quarterTwo);
        vendingMachine.insertCoin(nickelOne);



        List<Coin> returnedCoins = vendingMachine.returnCoins();

        assertNotNull(returnedCoins);
        assertSame(quarterOne, returnedCoins.get(0));
        assertSame(dimeOne, returnedCoins.get(1));
        assertSame(quarterTwo, returnedCoins.get(2));
        assertSame(nickelOne, returnedCoins.get(3));
    }

    @Test
    public void whenCoinsHaveBeenReturnedDisplayIsSetToInsertCoin(){
        Coin quarterOne = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);
        Coin quarterTwo = new Coin(CoinValidator.QUARTER_DIAMTER, CoinValidator.QUARTER_WIEGHT);
        Coin dimeOne = new Coin(CoinValidator.DIME_WIEGHT, CoinValidator.DIME_DIAMTER);
        Coin nickelOne = new Coin(CoinValidator.NICKEL_WIEGHT, CoinValidator.NICKEL_DIAMTER);
        vendingMachine.insertCoin(quarterOne);
        vendingMachine.insertCoin(dimeOne);
        vendingMachine.insertCoin(quarterTwo);
        vendingMachine.insertCoin(nickelOne);

        vendingMachine.returnCoins();

        verify(mockVendingMachineDisplayer, times(1)).updateDisplay("INSERT COIN");
    }

    @Test
    public void aNewlyIntializedVendingMachineHasNoChips(){
        assertEquals(0, vendingMachine.getChipsCount());

    }

    @Test
    public void aBagOfChipsCanBeStockedIntoTheVendingMachine(){
        List<Product> products = new ArrayList<>();
        products.add(Product.CHIPS);
        vendingMachine.addProduct(products);

        assertEquals(1, vendingMachine.getChipsCount());
    }

    @Test
    public void aCandyCanBeStockedIntoTheVendingMachine(){
        List<Product> products = new ArrayList<>();
        products.add(Product.CANDY);
        vendingMachine.addProduct(products);

        assertEquals(1, vendingMachine.getCandiesCount());
    }

    @Test
    public void aColaCanBeStockedIntoTheVendingMachine(){
        List<Product> products = new ArrayList<>();
        products.add(Product.COLA);
        vendingMachine.addProduct(products);

        assertEquals(1, vendingMachine.getColasCount());
    }

    @Test
    public void multipleBagsOfChipsCanBeAdded(){
        List<Product> products = new ArrayList<>();
        Product chipOne = Product.CHIPS;
        Product chipTwo = Product.CHIPS;
        Product chipThree = Product.CHIPS;
        products.add(chipOne);
        products.add(chipTwo);
        products.add(chipThree);

        vendingMachine.addProduct(products);

        assertEquals(3, vendingMachine.getChipsCount());
    }

    @Test
    public void multipleProductsCanBeAdded(){
        List<Product> products = new ArrayList<>();
        Product chipOne = Product.CHIPS;
        Product chipTwo = Product.CHIPS;
        Product chipThree = Product.CHIPS;
        products.add(chipOne);
        products.add(chipTwo);
        products.add(chipThree);
        Product candyOne = Product.CANDY;
        Product candyTwo = Product.CANDY;
        Product cola = Product.COLA;
        products.add(candyOne);
        products.add(candyTwo);
        products.add(cola);

        vendingMachine.addProduct(products);

        assertEquals(3, vendingMachine.getChipsCount());
        assertEquals(2, vendingMachine.getCandiesCount());
        assertEquals(1, vendingMachine.getColasCount());

    }






}