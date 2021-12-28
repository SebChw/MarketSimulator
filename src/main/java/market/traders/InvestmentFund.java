package market.traders;

import java.util.HashMap;
import java.util.ArrayList;
import market.assets.ProofOfPurchase;
import market.tradingSystems.Trade;
import market.assets.Currency;
public class InvestmentFund extends Trader {
    private String menagerFirstName;
    private String menagerLastName;
    private Trade fundTradingSystem;

    private HashMap<String, ArrayList<ProofOfPurchase>> transactions;

    public InvestmentFund(String tradingIdentifier, HashMap<Currency, Float> investmentBudget, String name,
                        String menagerFirstName, String menagerLastName){
        super(tradingIdentifier, investmentBudget, name, "Investment Fund");
        this.menagerFirstName = menagerFirstName;
        this.menagerLastName = menagerLastName;
    }

    public void trade(HumanInvestor investor){

    }
}
