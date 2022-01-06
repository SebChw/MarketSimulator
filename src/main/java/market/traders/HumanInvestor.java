package market.traders;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import market.assets.Currency;
import market.world.World;
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
    public void tradeOnInvestmentFund(InvestmentFund market){
        market.trade(this);
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
}
