package market.tradingSystems;

import market.markets.Market;
import market.traders.Trader;
public interface Trade {
    public void buy(Market market, Trader trader);
    public void sell(Market market, Trader trader);
}
