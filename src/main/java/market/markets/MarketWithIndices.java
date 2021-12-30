package market.markets;

import java.util.HashMap;

import market.traders.Trader;
import market.tradingSystems.Trade;
import market.assets.*;
import market.assets.marketIndex.StockMarketIndex;
public class MarketWithIndices<T extends Asset> extends Market<T>{
    //Basically we can buy indices on any kind of market.
    private HashMap<String, StockMarketIndex> indices;
    private Trade indexTradingSystem; 


    public MarketWithIndices(String name, String country, String city, String address, 
                    float percentageOperationCost, Currency tradingCurrency, HashMap<String, T> availableAssets){

        super(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets);

    }
    public void tradeIndices(Trader trader){

    }
    
}
