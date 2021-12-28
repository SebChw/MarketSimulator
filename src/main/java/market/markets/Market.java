package market.markets;

import market.assets.Currency;

import java.util.HashMap;
import java.util.ArrayList;
import market.traders.Trader;
import market.tradingSystems.Trade;
import market.assets.ProofOfPurchase;
import market.assets.Asset;

public class Market <T extends Asset> {
    private String name;
    private String country;
    private String city;
    private String address;
    private float percentageOperationCost;
    private Currency tradingCurrency;
    private String assetType;
    
    private HashMap<String, ArrayList<ProofOfPurchase>> transactions = new HashMap<String, ArrayList<ProofOfPurchase>>();
    private HashMap<String, Trader> clients = new HashMap<String, Trader>();
    private HashMap<String, T> assets = new HashMap<String, T>();
    
    private Trade tradingSystem;

    public Market(String name, String country, String city, String address, 
                float percentageOperationCost, Currency tradingCurrency){
        
        this.name=name;
        this.country=country;
        this.city=city;
        this.address=address;
        this.percentageOperationCost=percentageOperationCost;
        this.tradingCurrency=tradingCurrency;

    }

    public void trade(Trader trader){
        
    }
    
    public String getName(){
        return this.name;
    }
    public String getAssetType(){
        return this.assetType;
    }
    public void setAssetType(String assetType){
        this.assetType=assetType;
    }
    public String getCountry(){
        return this.country;
    }
    public String getCity(){
        return this.city;
    }
    public String getAddress(){
        return this.address;
    }

    public float getPercentageOperationCost(){
        return this.percentageOperationCost;
    }
    public String getCurrencyName(){
        return this.tradingCurrency.getName();
    }


}
