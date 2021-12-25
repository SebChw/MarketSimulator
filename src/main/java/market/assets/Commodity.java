package market.assets;

import market.assets.Currency;
import market.exchangeRates.ExchangeRatesProvider;
public class Commodity extends Asset {
    private String tradingUnit;
    private Currency tradingCurrency;
    //Ill read all these informations from ExchangeRatesProvider
    private int currentPrice;
    private int minPrice;
    private int maxPrice;

    public Commodity(String name, String tradingUnit, Currency tradingCurrency, ExchangeRatesProvider mainBankRates) {
        super(name, mainBankRates);
        this.tradingUnit = tradingUnit;
        this.tradingCurrency = tradingCurrency;
    }

    @Override
    public String toString(){
        return super.toString() + "\ntradingUnit:" + this.tradingUnit + "\ntradingCurrency: " + this.tradingCurrency.getName();
    }

    @Override
    public void plotAssetValueOverTime(Asset versus){

    }

}
