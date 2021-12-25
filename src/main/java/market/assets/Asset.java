package market.assets;

import java.util.ArrayList;
import market.observers.*;
import market.exchangeRates.ExchangeRatesProvider;
/**
 * This is root class for all assets available in the market
 * 
 */
public abstract class Asset implements AssetSubject{
    private String name;
    private float amountInCirculation;
    private int hypeLevel; // maybe MarketPriceRule will be dependend on that
    private int amountOfOwners; // maybe MarkerPriceRule will be dependent on that
    private ExchangeRatesProvider mainBankRates;
    private ArrayList<AssetObserver> observers; // Abservers that check what's happening with that asset to make some changes about it

    public Asset(String name, ExchangeRatesProvider mainBankRates){
        this.name = name;
        this.amountInCirculation = 0;
        this.hypeLevel = 0; // I think these should be set to 0 at the beginning 
        this.amountOfOwners = 0; // I think these should be set to 0 at the beginning
        this.observers = new ArrayList<AssetObserver>();
        this.mainBankRates  = mainBankRates ;
    }   

    public String getName(){
        return this.name;
    }
    
    public void plotAssetValueOverTime(Asset versus){

    }

    @Override 
    public String toString(){
        return "Asset name: " + this.name + "\n amount in circulation: " + this.amountInCirculation + "\n hype Level" + this.hypeLevel + "\n amount of owners: " + this.amountOfOwners;
    }

    public void updateRate(String nameOfAsset, Float rate){
        this.mainBankRates.updateRate(nameOfAsset, rate);
    }

    @Override
    public void notifyObservers() {
        for (AssetObserver assetObserver : observers) {
            assetObserver.update(this.name, this.hypeLevel, this.amountOfOwners, this.amountInCirculation);
        }
    }

    @Override
    public void registerObserver(AssetObserver observer) {
        this.observers.add(observer); // Now we will notify that observer
    }

    @Override
    public void removeObserver(AssetObserver observer) {
        this.observers.remove(observer);
        
    }
    
}

