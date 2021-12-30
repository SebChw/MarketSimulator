package market.assets;

import java.util.LinkedList;
import market.exchangeRates.ExchangeRatesProvider;
public abstract class NonCurrencyAsset extends Asset{
    private Currency tradingCurrency;
    private LinkedList<Float> exchangeRateToTradingCurrency = new LinkedList<Float>();


    public NonCurrencyAsset(String name, String type, float amountInCirculation, Currency tradingCurrency, Float startingRate) {
        super(name, type, amountInCirculation);
        this.tradingCurrency = tradingCurrency;
        this.exchangeRateToTradingCurrency.addFirst(startingRate);
    }

    @Override
    public String toString(){
        return super.toString() + "\ntradingCurrency:" + this.tradingCurrency.getName();
    }
    @Override
    public float calculateThisToMain(float amount) {
        //Here we must do This asset -> this Asset's Currency -> Main Backing Currency
        float thisAssetToThisCurrency = amount * this.exchangeRateToTradingCurrency.getFirst();
        return this.tradingCurrency.calculateThisToMain(thisAssetToThisCurrency);
    }

    public float calculateMainToThis(float amount){
        //Here we must do Main -> this Asset's Currency -> This asset
        float mainToThisCurrency = this.tradingCurrency.calculateMainToThis(amount);
        return mainToThisCurrency * 1/this.exchangeRateToTradingCurrency.getFirst();
    }
    
}
