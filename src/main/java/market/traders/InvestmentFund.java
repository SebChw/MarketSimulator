package market.traders;

import java.util.HashMap;
import java.util.ArrayList;
import market.assets.ProofOfPurchase;
import market.tradingSystems.Trade;
import market.assets.Currency;
import market.App;
public class InvestmentFund extends Trader {
    private String menagerFirstName;
    private String menagerLastName;
    private Trade fundTradingSystem;
    private Currency registeredCurrency;

    private HashMap<String, ArrayList<ProofOfPurchase>> transactions;

    public InvestmentFund(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
                        String menagerFirstName, String menagerLastName, Currency registeredCurrency){
        super(tradingIdentifier, investmentBudget, name, "Investment Fund");
        this.menagerFirstName = menagerFirstName;
        this.menagerLastName = menagerLastName;
        this.registeredCurrency = registeredCurrency;

        IssueFundUnit();
    }

    public void trade(HumanInvestor investor){

    }

    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    public void IssueFundUnit(){
        App.addInvestmentFundUnit(this);
    }
}
