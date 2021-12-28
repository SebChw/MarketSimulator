package market.markets;

import java.util.HashMap;
import market.marketIndex.StockMarketIndex;
import market.traders.Trader;
import market.tradingSystems.Trade;
import market.assets.*;
public class MarketWithIndices<T extends Asset> extends Market<T>{
    //Basically we can buy indices on any kind of market.
    private HashMap<String, StockMarketIndex> indices;
    private Trade indexTradingSystem; 


    public MarketWithIndices(String name, String country, String city, String address, 
                    float percentageOperationCost, Currency tradingCurrency){

        super(name, country, city, address, percentageOperationCost, tradingCurrency);

    }
    public void tradeIndices(Trader trader){

    }
    
}
