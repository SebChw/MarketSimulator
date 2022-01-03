package market.assets;

import java.util.HashSet;

import market.exchangeRates.ExchangeRatesProvider;
import market.observers.AssetObserver;
import market.App;
import market.entityCreator.SemiRandomValuesGenerator;
public class Currency extends Asset {
    private HashSet<String> countriesWhereLegal;
    
    public Currency(String name, HashSet<String> countriesWhereLegal, String backingAsset, float startingRate) {
        super(name, "Currency", 0, backingAsset, startingRate);
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
