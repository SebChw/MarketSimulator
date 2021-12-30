package market.assets;

import market.assets.Currency;
import market.exchangeRates.ExchangeRatesProvider;
import java.util.*;
public class Commodity extends NonCurrencyAsset {
    private String tradingUnit;
    //Ill read all these informations from ExchangeRatesProvider
    private float currentPrice; // this I will make functions for!
    private float minPrice;
    private float maxPrice;

    public Commodity(String name, String tradingUnit, Currency tradingCurrency, Float startingRate) {
        //At the beginning there is no commopdities in circulation everything start when some user buy it.
        super(name, "Commodity", 0, tradingCurrency, startingRate);
        this.tradingUnit = tradingUnit;
    }

    @Override
    public String toString(){
        return super.toString() + "\ntradingUnit:" + this.tradingUnit;
    }

    @Override
    public void plotAssetValueOverTime(Asset versus){

    }

}
