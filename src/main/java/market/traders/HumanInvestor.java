package market.traders;

import java.util.HashMap;
import market.assets.Currency;
public class HumanInvestor extends Trader {
    private String lastName;

    public HumanInvestor(String tradingIdentifier, HashMap<Currency, Float> investmentBudget, String name, String lastName){
        super(tradingIdentifier, investmentBudget, name);
        this.lastName = lastName;
    }

    public void tradeOnInvestmentFund(InvestmentFund market){
        market.trade(this);
    }
}
