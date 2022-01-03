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
    private String type;
    private int hypeLevel; // maybe MarketPriceRule will be dependend on that
    private int amountOfOwners; // maybe MarkerPriceRule will be dependent on that
    private ArrayList<AssetObserver> observers; // Abservers that check what's happening with that asset to make some changes about it
    private ExchangeRatesProvider mainBankRates;

    public Asset(String name, String type, float amountInCirculation, String backingAsset, float startingRate){
        this.name = name;
        this.type = type;
        this.amountInCirculation = amountInCirculation;
        this.hypeLevel = 0; // I think these should be set to 0 at the beginning 
        this.amountOfOwners = 0; // I think these should be set to 0 at the beginning
        this.observers = new ArrayList<AssetObserver>();
        this.mainBankRates = new ExchangeRatesProvider(backingAsset, startingRate);
    }   
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public float getAmountInCirculation(){
        return this.amountInCirculation;
    }
    public int getAmountOfOwners(){
        return this.amountOfOwners;
    }
    public int getHypeLevel(){
        return this.hypeLevel;
    }
    
    public void plotAssetValueOverTime(Asset versus){

    }

    public void increaseAmountInCirculation(int amount){
        this.amountInCirculation += amount;
    }

    @Override 
    public String toString(){
        return "Asset name: " + this.name + "\n amount in circulation: " + this.amountInCirculation + "\n hype Level" + this.hypeLevel + "\n amount of owners: " + this.amountOfOwners;
    }

    public float calculateThisToMain(float amount) {
        return 1/this.mainBankRates.getRate() * amount;
    }

    public float calculateMainToThis(float amount) {
        return this.mainBankRates.getRate() * amount;
    }

    public ExchangeRatesProvider getMainBankRates(){
        return this.mainBankRates;
    }

    public float getCurrentRate() {
        return this.mainBankRates.getRate();
    }

    public float calculateThisToDifferent(Asset tradedAsset, float amount){
        //Here I could just do division However I think this is more clear and only up to 2 operations more
        //Also THis is more beneficial It I have some more complicated Assets Like marketIndex for whom calculating 
        //we can't just get these two ratios that easily.
        return tradedAsset.calculateMainToThis(this.calculateThisToMain(amount));
    };
    public float calculateDifferentToThis(Asset tradedAsset, float amount){
        return this.calculateMainToThis(tradedAsset.calculateThisToMain(amount));
    };

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

