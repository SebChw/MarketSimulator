package market.traders;
import market.markets.Market;
import java.util.HashMap;
import market.assets.Currency;

public class Trader {
    private String tradingIdentifier;
    private HashMap<Currency, Float> investmentBudget;
    private String name;
    private String type;

    public Trader(String tradingIdentifier, HashMap<Currency, Float> investmentBudget, String name, String type){
        this.tradingIdentifier = tradingIdentifier;
        this.investmentBudget = investmentBudget;
        this.name = name;
        this.type= type;
    }

    public void tradeOnMarket(Market<?> market){
        market.trade(this);
    }

    public String getName(){
        return this.name;
    }

    public String getTradingIdentifier(){
        return this.tradingIdentifier;
    }

    public String getType(){
        return this.type;
    }
    public float getBudgetInGold(){
        return 1000;
    }

    public void IncreaseBudget(){

    }
    

}
