package market.markets;

import market.assets.Currency;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import market.traders.Trader;
import market.tradingSystems.Trade;
import market.assets.ProofOfPurchase;
import market.assets.Asset;
import market.App;

public class Market <T extends Asset> {
    private String name;
    private String country;
    private String city;
    private String address;
    private float percentageOperationCost;
    private Currency tradingCurrency;
    private String assetType;
    
    private HashMap<Trader, ArrayList<ProofOfPurchase>> clients = new HashMap<Trader, ArrayList<ProofOfPurchase>>();
    private HashMap<String, T> availableAssets = new HashMap<String, T>(); // Maybe make this justa HashSet ?
    
    private Trade tradingSystem;

    public Market(String name, String country, String city, String address, 
                float percentageOperationCost, Currency tradingCurrency, HashMap<String, T> availableAssets){
        
        this.name=name;
        this.country=country;
        this.city=city;
        this.address=address;
        this.percentageOperationCost=percentageOperationCost;
        this.tradingCurrency=tradingCurrency;
        this.availableAssets = availableAssets;

    }

    public void buy(Trader trader, Asset wantedAsset, float amount){
        if (!this.availableAssets.containsKey(wantedAsset.getName())){
            System.out.println("We don't have such asset here!");
            return;
        }
        //!Trading currencies and trading commodities is a little bit different! Thats why I need that interface
        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        String tradingCurrencyName = this.tradingCurrency.getName();
        float cost = this.tradingCurrency.calculateDifferentToThis(wantedAsset, amount); // how much of our trading currency we must pay for this asset!
        cost += cost*this.percentageOperationCost; // we need to increase the cost
        if (traderInvestmentBudget.containsKey(tradingCurrencyName) && traderInvestmentBudget.get(tradingCurrencyName) >= cost){
            float tradingCurrencyAmount = traderInvestmentBudget.get(tradingCurrencyName);
            T asset = availableAssets.get(wantedAsset.getName());
           
            if (cost <= tradingCurrencyAmount){
                trader.addBudget(wantedAsset.getName(), amount);
                trader.subtractBudget(tradingCurrencyName, cost);
            }

        }
        else {
            System.out.println("You don't have enough" + this.tradingCurrency.getName() + " We will exchange for free all your assets to our trading Currency until you have enough!");
            float tradingCurrencyAmount = 0;
            HashMap<String, Float> traderInvestmentBudgetCopy = new HashMap<String, Float>(trader.getInvestmentBudget());
            Iterator<Map.Entry<String, Float>> it = traderInvestmentBudgetCopy.entrySet().iterator(); //!Here I need to copy this as Im changing it within loop
            Map.Entry<String, Float> budget = null; 
            while (tradingCurrencyAmount < cost && it.hasNext()){
                budget = it.next();
                //!These two operations must be done in that order as at first I may subtract and then remove item from the dictonary!!!
                tradingCurrencyAmount += this.tradingCurrency.calculateDifferentToThis(App.getParticularAsset(budget.getKey()), budget.getValue());
                trader.subtractBudget(budget.getKey(), budget.getValue()); // I take all money he has here! So it is equal to removing this entry from HashMap
            }
            if (tradingCurrencyAmount > cost){
                tradingCurrencyAmount -= cost;
                trader.addBudget(wantedAsset.getName(), amount);
                trader.addBudget(this.tradingCurrency.getName(), tradingCurrencyAmount); // It can't be null here! I'm returning back what has left in trading Currency
            }
            else if (tradingCurrencyAmount < cost){
                System.out.println("You still don't have enough money. We will exchange all you have for the chosen asset");
                //Now I treat tradingCurrencyAmount as cost + provision. So I need to get cost back
                tradingCurrencyAmount = tradingCurrencyAmount / (1 + this.percentageOperationCost);
                trader.addBudget(wantedAsset.getName(), this.tradingCurrency.calculateThisToDifferent(wantedAsset, tradingCurrencyAmount));
            }
            else{
                //If they are equal I just add wantedAsset
                trader.addBudget(wantedAsset.getName(), amount);
            }
        }
    }

    public void sell(Trader trader, Asset soldAsset, float amount){
        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        //That asset must be available in both Market and in Trader and trader must have enough of that asset!
        if(traderInvestmentBudget.containsKey(soldAsset.getName()) && traderInvestmentBudget.get(soldAsset.getName()) >= amount &&this.availableAssets.containsKey(soldAsset.getName())){
            float income = this.tradingCurrency.calculateDifferentToThis(soldAsset, amount);
            income = income / (1 + this.percentageOperationCost);
            trader.addBudget(this.tradingCurrency.getName(), income);
            trader.subtractBudget(soldAsset.getName(), amount); 
        }
        else{
            System.out.println("Either you don't have asset to be sold or this Market does not support it!");
        }
        
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

    public HashMap<String, T> getAvailableAssets(){
        return this.availableAssets;
    }

}
