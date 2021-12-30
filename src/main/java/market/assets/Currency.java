package market.assets;

import java.util.HashSet;

import market.exchangeRates.ExchangeRatesProvider;
import market.observers.AssetObserver;

public class Currency extends Asset {
    private HashSet<String> countriesWhereLegal;
    private ExchangeRatesProvider mainBankRates;

    public Currency(String name, HashSet<String> countriesWhereLegal, ExchangeRatesProvider mainBankRates) {
        //At the very beginning there is no Currency in Circulation, everything starts when we fill the budget of our users
        super(name, "Currency", 0);
        this.mainBankRates = mainBankRates;
        this.countriesWhereLegal = countriesWhereLegal;
    }

    @Override
    public float calculateThisToMain(float amount) {
        return 1/this.mainBankRates.getRate(this.getName()) * amount;
    }

    @Override
    public float calculateMainToThis(float amount) {
        return this.mainBankRates.getRate(this.getName()) * amount;
    }

    @Override
    public String toString(){
        return super.toString() + "\nLegal in: " + countriesWhereLegal;
    }

    @Override
    public void plotAssetValueOverTime(Asset versus){

    }

    public ExchangeRatesProvider getMainBankRates(){
        return this.mainBankRates;
    }


    
}
