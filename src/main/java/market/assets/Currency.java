package market.assets;

import java.util.HashSet;

import market.exchangeRates.ExchangeRatesProvider;
import market.observers.AssetObserver;

public class Currency extends Asset {
    private HashSet<String> countriesWhereLegal;
    

    public Currency(String name, HashSet<String> countriesWhereLegal, ExchangeRatesProvider mainBankRates) {
        super(name, "Currency", mainBankRates);
        this.countriesWhereLegal = countriesWhereLegal;
    }

    @Override
    public String toString(){
        return super.toString() + "\nLegal in: " + countriesWhereLegal;
    }

    @Override
    public void plotAssetValueOverTime(Asset versus){

    }

    
}
