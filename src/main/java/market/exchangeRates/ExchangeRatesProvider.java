package market.exchangeRates;

import java.util.HashMap;
import java.util.ArrayList;
import market.marketRules.MarketPriceRule;
import market.observers.AssetObserver;
import java.util.LinkedList;

public class ExchangeRatesProvider implements AssetObserver{
    private String nameOfBackingAsset;
    private MarketPriceRule marketRule; 
    private LinkedList<Float> rates = new LinkedList<Float>(); // This provides exchange rates of one asset to all other


    public ExchangeRatesProvider(String nameOfBackingAsset, float startingPrice){
        this.nameOfBackingAsset = nameOfBackingAsset; // Main asset that Backs everything in our system
        this.updateRate(startingPrice);
    }

    public void updateRate(Float rate){
        this.rates.addFirst(rate); // Consider here inserting new rate at the begining so taking it will be O(1). //!Add FIRST METHOD
    }

    public String getNameOfBackingAsset(){
        return this.nameOfBackingAsset;
    }
    
    public float getRate(){
        return this.rates.getFirst(); //!Consider using GetFirst here and appending new raters at the beginning!
    }

    public void removeLastRate(){
        this.rates.removeLast();
    }

    @Override
    public void update(String assetName, int hypeLevel, int amountOfOwners, float AmountInCirculation) {
        if (assetName == this.nameOfBackingAsset){
            //Do for loop over all assets here
            //use this class updateRate function!
        }
        else {
            //Here we just update asset that we are concerned about
        }
        
    }
}