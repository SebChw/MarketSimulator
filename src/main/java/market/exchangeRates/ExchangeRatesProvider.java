package market.exchangeRates;

import java.util.HashMap;
import java.util.ArrayList;
import market.marketRules.MarketPriceRule;
import market.observers.AssetObserver;
import java.util.LinkedList;

public class ExchangeRatesProvider implements AssetObserver{
    private String nameOfAsset;
    private String typeOfAsset;
    private MarketPriceRule marketRule; 
    private HashMap<String, LinkedList<Float>> rates; // This provides exchange rates of one asset to all other


    public ExchangeRatesProvider(String nameOfAsset, String typeOfAsset){
        this.nameOfAsset = nameOfAsset;
        this.typeOfAsset = typeOfAsset;

        this.rates = new HashMap<String, LinkedList<Float>>();
    }

    public void updateRate(String nameOfAsset, Float rate){
        LinkedList<Float> currentRates = this.rates.get(nameOfAsset);
        if (currentRates == null){
            currentRates = new LinkedList<Float>();
        }
        currentRates.add(rate); // Consider here inserting new rate at the begining so taking it will be O(1). //!Add FIRST METHOD
        this.rates.put(nameOfAsset, currentRates); // This will suceed either tu put rate for the first time or to update it
    }

    public String getNameOfAsset(){
        return this.nameOfAsset;
    }
    
    public float getRate(String nameOfAsset){
        return this.rates.get(nameOfAsset).getLast(); //!Consider using GetFirst here and appending new raters at the beginning!
    }

    @Override
    public void update(String assetName, int hypeLevel, int amountOfOwners, float AmountInCirculation) {
        if (assetName == this.nameOfAsset){
            //Do for loop over all assets here
            //use this class updateRate function!
        }
        else {
            //Here we just update asset that we are concerned about
        }
        
    }
}