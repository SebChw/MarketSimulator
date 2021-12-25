package market.traders;
import market.markets.Market;
import java.util.HashMap;
import market.assets.Currency;

public class Trader {
    private String tradingIdentifier;
    private HashMap<Currency, Float> investmentBudget;
    private String name;

    public Trader(String tradingIdentifier, HashMap<Currency, Float> investmentBudget, String name){
        this.tradingIdentifier = tradingIdentifier;
        this.investmentBudget = investmentBudget;
        this.name = name;
    }

    public void tradeOnMarket(Market market){
        market.trade(this);
    }

    public String getName(){
        return this.name;
    }

    public void IncreaseBudget(){

    }
    

}
