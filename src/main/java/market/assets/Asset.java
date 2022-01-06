package market.assets;

import java.util.ArrayList;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import market.observers.*;
import market.exchangeRates.ExchangeRatesProvider;
import market.priceRules.*;
/**
 * This is root class for all assets available in the market
 * 
 */
public abstract class Asset{
    private String name;
    private volatile SimpleFloatProperty amountInCirculation;
    private String type;
    private volatile SimpleFloatProperty hypeLevel = new SimpleFloatProperty(0); // maybe MarketPriceRule will be dependend on that
    private volatile SimpleIntegerProperty amountOfOwners = new SimpleIntegerProperty(0); // maybe MarkerPriceRule will be dependent on that
    private ExchangeRatesProvider mainBankRates;

    public Asset(String name, String type, float amountInCirculation, String backingAsset, float startingRate){
        this.name = name;
        this.type = type;
        this.amountInCirculation = new SimpleFloatProperty(amountInCirculation);
        this.mainBankRates = new ExchangeRatesProvider(backingAsset, startingRate);
    }   
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public SimpleFloatProperty amountInCirculationProperty(){
        return this.amountInCirculation;
    }
    public IntegerProperty amountOfOwnersProperty(){
        return this.amountOfOwners;
    }
    
    public SimpleFloatProperty hypeLevelProperty(){
        return this.hypeLevel;
    }
    
    public void plotAssetValueOverTime(Asset versus){

    }

    public synchronized void changeAmountInCirculation(float amount){
        float newValue = this.amountInCirculation.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.amountInCirculation.get() , "amount");
        this.amountInCirculation.set(newValue);
    }

    public synchronized void changeHypeLevel(float amount){
        float newValue = this.hypeLevel.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.hypeLevel.get() , "hypeLevel");
        this.hypeLevel.set(newValue);
        
    }

    public synchronized void changeAmountOfOwners(int amount){
        int newValue = this.amountOfOwners.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.amountOfOwners.get(), "owners");
        this.amountOfOwners.set(newValue);
        
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
    public void updateRate(){
        mainBankRates.updateRate();
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

    
}

