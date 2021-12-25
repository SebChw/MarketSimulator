package market.markets;

import java.util.HashMap;
import market.marketIndex.StockMarketIndex;
import market.traders.Trader;
import market.tradingSystems.Trade;

public class StockMarket extends Market{
    private HashMap<String, StockMarketIndex> indices;
    private Trade indexTradingSystem; 

    public void tradeIndices(Trader trader){

    }
    
}
