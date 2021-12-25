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
    private HashMap<String, ArrayList<ProofOfPurchase>> transactions;

    private Currency tradingCurrency;
    
    private HashMap<String, Trader> clients;
    private HashMap<String, T> assets;
    
    private Trade tradingSystem;

    public void trade(Trader trader){
        
    }

}
