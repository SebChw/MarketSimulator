package market.traders;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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

    private String [] moreDetails = {"Menager first name: ", "Menager last name: ", "Registered currency: "}; 
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

    public void fillGridPane(GridPane traderDetails){
        super.fillGridPane(traderDetails);
        String [] filledDetails = {menagerFirstName, menagerLastName, registeredCurrency.getName()};
        for (int i = 0; i < filledDetails.length; i++) {
            Label l = new Label();
            l.setText(moreDetails[i] + filledDetails[i]);
            traderDetails.add(l, 0, i + 3);
        }
    }
}
