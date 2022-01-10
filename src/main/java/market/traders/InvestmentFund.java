package market.traders;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import market.assets.ProofOfPurchase;
import market.gui.MainPanelController;
import market.world.World;
import market.assets.Currency;
import market.assets.InvestmentFundUnit;
import market.App;
import market.entityCreator.*;
public class InvestmentFund extends Trader {
    private String menagerFirstName;
    private String menagerLastName;
    private Currency registeredCurrency;
    private int issuedFundsAmount = 0;
    private ArrayList<InvestmentFundUnit> issuedFundsUnit = new ArrayList<InvestmentFundUnit>();

    private String [] moreDetails = {"Menager first name: ", "Menager last name: ", "Registered currency: "}; 
    private MainPanelController controller;

    public InvestmentFund(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
                        String menagerFirstName, String menagerLastName, Currency registeredCurrency, boolean isBear, World world, MainPanelController controller){
        super(tradingIdentifier, investmentBudget, name, "Investment Fund", isBear, world);
        this.menagerFirstName = menagerFirstName;
        this.menagerLastName = menagerLastName;
        this.registeredCurrency = registeredCurrency;
        this.controller = controller;

        IssueFundUnit();
    }

    public boolean checkConstraints(Trader trader, InvestmentFundUnit wantedUnit){

        if (!trader.getType().equals("human Investor")){
            System.out.println("Only Human Investors Can buy here!!");
            return false;
        }

        if (!this.issuedFundsUnit.contains(wantedUnit)){
            System.out.println("We don't have such asset here!");
            return false;
        }

        return true;
    }

    public void buy(Trader trader, InvestmentFundUnit wantedUnit, float amount){
        //! With that trading system only with good currency you can but anything that may lead to no buy of some UNIT at all :(
        if (!checkConstraints(trader, wantedUnit)) return;

        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        String registeredCurrencyName = this.registeredCurrency.getName();
        float cost = this.registeredCurrency.calculateDifferentToThis(wantedUnit, amount); // how much of our trading currency we must pay for this asset!

        if (traderInvestmentBudget.containsKey(registeredCurrencyName) && traderInvestmentBudget.get(registeredCurrencyName) >= cost){
            trader.addBudget(wantedUnit, amount);
            trader.subtractBudget(registeredCurrency, cost); //! This yield to negative Amount in circulation of an asset as we Subtract more of it then we possible bought previously!!.
        }
        else {
            //System.out.println(this.registeredCurrency.getName() + " " + cost + " " + traderInvestmentBudget.get(registeredCurrencyName));
            System.out.println("Not enough money to buy on investment fund! Or you just don't have our currency!");
        }
    }

    public void sell(Trader trader, InvestmentFundUnit soldUnit, float amount){
        if (!checkConstraints(trader, soldUnit)) return;

        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        float income = this.registeredCurrency.calculateDifferentToThis(soldUnit, amount);
        trader.addBudget(this.registeredCurrency, income);
        trader.subtractBudget(soldUnit, amount);
        
    }

    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    public void IssueFundUnit(){
        issuedFundsAmount +=1;
        System.out.println("Fund: " + this.getName() + "Issue new unit");
        issuedFundsUnit.add(super.getWorld().addNewInvestmentFundUnit(this));
    }

    public int getIssuedFundsAmount(){
        return issuedFundsAmount;
    }

    public void fillGridPane(GridPane traderDetails){
        super.fillGridPane(traderDetails);
        String [] filledDetails = {menagerFirstName, menagerLastName, registeredCurrency.getName()};
        for (int i = 0; i < filledDetails.length; i++) {
            Label l = new Label();
            l.setText(moreDetails[i] + filledDetails[i]);
            traderDetails.add(l, 0, i + 4);
        }
    }
    
    public MainPanelController getController(){
        return controller;
    }

    @Override
    public void operation(){
        super.operation();
        //! I tried to issue funds here however I got error from JavaFX that I'm trying to change it from different thread etc.
    }

    public ArrayList<InvestmentFundUnit> getIssuedFundsUnit(){
        return issuedFundsUnit;
    }
}
