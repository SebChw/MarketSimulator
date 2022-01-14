package market.traders;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.GridPane;
import market.world.World;
import market.entityCreator.*;

/**
 * Class representing investor that can buy on the investment fund
 */
public class HumanInvestor extends Trader {
    private String lastName;
    private String[] moreDetails = { "last Name: " };

    public HumanInvestor(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
            String lastName, boolean isBear, World world) {
        super(tradingIdentifier, investmentBudget, name, "human Investor", isBear, world);
        this.lastName = lastName;
    }

    /**
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Function that automatically trades on randomly selected investment fund
     * 
     * @param operation "buy" or "sell"
     */
    public void tradeOnInvestmentFund(String operation) {
        ArrayList<InvestmentFund> availableFunds = this.getWorld().getWorldContainer().getInvestmentFunds();
        if (availableFunds.isEmpty()) {
            return;
        }
        InvestmentFund fund = availableFunds.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableFunds));
        super.tradeOn(operation, fund);
    }

    /**
     * @param traderDetails
     */
    @Override
    public void fillGridPane(GridPane traderDetails) {
        super.fillGridPane(traderDetails);
        String[] filledDetails = { lastName };
        super.addLabelsToPane(traderDetails, moreDetails, filledDetails, 4);

    }

    /**
     * Human investor additionally can buy or sell things in Investment Fund
     */
    @Override
    public void operation() {
        super.operation();
        float transactionProbability = this.getWorld().getTransactionProbability();
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability) {
            tradeOnInvestmentFund("buy");
        }
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability) {
            tradeOnInvestmentFund("sell");
        }

    }

}
