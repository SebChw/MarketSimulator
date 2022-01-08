package market.traders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import market.assets.Currency;
import market.world.World;
import market.entityCreator.*;
import market.assets.InvestmentFundUnit;
public class HumanInvestor extends Trader {
    private String lastName;
    private String [] moreDetails = {"last Name: "};

    public HumanInvestor(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name, String lastName, boolean isBear, World world){
        super(tradingIdentifier, investmentBudget, name, "human Investor", isBear, world);
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }
    public void tradeOnInvestmentFund(String operation){
        System.out.println("Me " + this.getName() + " Trading on InvestmentFund!");
        ArrayList<InvestmentFund> availableFunds = this.getWorld().getInvestmentFunds();
        if (availableFunds.isEmpty()){
            System.out.println("No funds Available for me :(");
            return;
        }
        InvestmentFund fund = availableFunds.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableFunds));
        
        if (operation == "buy") {
            ArrayList<InvestmentFundUnit> availableUnits = fund.getIssuedFundsUnit();
            InvestmentFundUnit chosenInvestmentFundUnit = availableUnits.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableUnits));
            fund.buy(this, chosenInvestmentFundUnit, chosenInvestmentFundUnit.getPossibleAmount(50));
        } else{
            List<InvestmentFundUnit> availableUnits = fund.getIssuedFundsUnit().stream()
                                .filter(a -> this.getInvestmentBudget().containsKey(a.getName())).collect(Collectors.toList());
            if (!availableUnits.isEmpty()) {
                InvestmentFundUnit chosenInvestmentFundUnit = availableUnits.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableUnits));
                
                float chosenAmount = chosenInvestmentFundUnit.getPossibleAmount(this.getInvestmentBudget().get(chosenInvestmentFundUnit.getName()));
                fund.sell(this, chosenInvestmentFundUnit, chosenAmount);
            }
        }   
    }

    @Override
    public void fillGridPane(GridPane traderDetails){
        super.fillGridPane(traderDetails);
        String [] filledDetails = {lastName};
        for (int i = 0; i < filledDetails.length; i++) {
            Label l = new Label();
            l.setText(moreDetails[i] + filledDetails[i]);
            traderDetails.add(l, 0, i + 4);
        }
        
    }

    @Override
    public void operation(){
        super.operation();
        float transactionProbability = this.getWorld().getTransactionProbability();
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability){
            tradeOnInvestmentFund("buy");
        }
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability){
            tradeOnInvestmentFund("sell");
        }

    }
    
}
