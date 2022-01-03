package market.traders;
import market.markets.Market;
import java.util.HashMap;
import java.util.Objects;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import market.assets.Currency;

public class Trader {
    private String tradingIdentifier;
    private HashMap<String, Float> investmentBudget;
    private String name;
    private String type;
    private String [] details = {"Id: ", "Type: ", "Name: "};
    public Trader(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name, String type){
        this.tradingIdentifier = tradingIdentifier;
        this.investmentBudget = investmentBudget;
        this.name = name;
        this.type= type;
    }
    
    @Override
    public String toString(){
        return this.tradingIdentifier + " " + this.type + " " + this.name; 
    }
    public void buyOnMarket(Market market){
        //market.buy(this);
    }

    public void sellOnMarket(Market market){
        //market.sell(this);
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
    
    public HashMap<String, Float> getInvestmentBudget(){
        return this.investmentBudget;
    }

    public void addBudget(String assetName, float amount){
        float currentBudget = this.investmentBudget.getOrDefault(assetName, (float)0);
        investmentBudget.put(assetName, currentBudget + amount);
    }

    public void subtractBudget(String assetName, float amount){
        //!Throw exception here!
        float currentBudget = this.investmentBudget.get(assetName);
        if (Objects.isNull(currentBudget) || currentBudget < amount) {
            System.out.println("Logic Error!");
        }
        else{
            currentBudget -= amount;
            if (currentBudget < 0.1){
                this.investmentBudget.remove(assetName);
            }
            else{
                this.investmentBudget.put(assetName, currentBudget);
            }
        }
    }

    public void fillGridPane(GridPane traderDetails){
        String [] filledDetails = {tradingIdentifier, type, name};
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            traderDetails.add(l, 0, i);
        }
    }
}
