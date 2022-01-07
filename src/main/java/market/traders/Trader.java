package market.traders;
import market.markets.Market;
import market.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import market.App;
import market.assets.Asset;
import market.assets.Currency;
import market.entityCreator.SemiRandomValuesGenerator;
public class Trader  implements Runnable{
    private String tradingIdentifier;
    private HashMap<String, Float> investmentBudget;
    private String name;
    private String type;
    private String [] details = {"Id: ", "Type: ", "Name: ", "Is Bear: "};
    Boolean isBear;
    private World world;
    public Trader(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name, String type, boolean isBear, World world) {
        this.tradingIdentifier = tradingIdentifier;
        this.investmentBudget = investmentBudget;
        this.name = name;
        this.type= type;
        this.isBear = isBear;
        this.world = world;
    }
    
    @Override
    public String toString(){
        return this.tradingIdentifier + " " + this.type + " " + this.name; 
    }
    public World getWorld(){
        return this.world;
    }

    public void tradeOnMarket(String operation){
        ArrayList<Market> availableMarkets = world.getAllMarkets();
        Market market = availableMarkets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableMarkets));
        
        if (operation == "buy") {
            ArrayList<Asset> availableAssets = market.getAvailableAssets();
            Asset chosenAsset = availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets));
            market.buy(this, chosenAsset, chosenAsset.getPossibleAmount());
        } else{
            List<Asset> availableAssets = market.getAvailableAssets().stream()
                                .filter(a -> this.investmentBudget.containsKey(a.getName())).collect(Collectors.toList());
            if (!availableAssets.isEmpty()) {
                Asset chosenAsset = availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets));

                if(Objects.isNull(investmentBudget.get(chosenAsset.getName()))) return;
                if (investmentBudget.get(chosenAsset.getName()) == 0){
                    investmentBudget.remove(chosenAsset.getName());
                    return;
                } 
                
                float chosenAmount = chosenAsset.getPossibleAmount(investmentBudget.get(chosenAsset.getName()));
                market.sell(this, chosenAsset, chosenAmount);
            }
        }   
    }

    public void tradeOnMarket(String operation, Market market){
        ArrayList<Asset> availableAssets = market.getAvailableAssets();
        if (operation == "buy") {
            market.buy(this, availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets)), 100);
        } else{
            market.sell(this, availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets)), 100);
        }

    }

    public void increaseBudget(){
        ArrayList<Currency> availableCurrencies = this.world.getCurrencies();
        Currency randomCurrency = availableCurrencies.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableCurrencies));
        
        this.addBudget(randomCurrency, 1000); 
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
    
    public HashMap<String, Float> getInvestmentBudget(){
        return this.investmentBudget;
    }

    public void addBudget(Asset asset, float amount){
        //!I'm updating all assset information here
        asset.changeAmountInCirculation(amount);
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) > 0.5){
            asset.changeHypeLevel(SemiRandomValuesGenerator.getFromNormal(amount / 10, 5));
        }
        asset.changeAmountOfOwners(notPosses(asset));

        String assetName = asset.getName();
        float currentBudget = this.investmentBudget.getOrDefault(assetName, (float)0);
        investmentBudget.put(assetName, currentBudget + amount);
    }

    public void subtractBudget(Asset asset, float amount){
        String assetName = asset.getName();
        //!Throw exception here!
        float currentBudget = this.investmentBudget.get(assetName);
        if (Objects.isNull(currentBudget) || currentBudget < amount) {
            System.out.println("Logic Error!");
        }
        else{
            currentBudget -= amount;
            if (currentBudget < 0.1){
                System.out.println("Removing from budget: " + assetName);
                //!This may lead to bug with shares
                amount += currentBudget; // Since I'm removing it from trader I also should remove same amount from circulation!
                this.investmentBudget.remove(assetName);
            }
            else{
                this.investmentBudget.put(assetName, currentBudget);
            }
        }

        asset.changeAmountInCirculation(-amount);
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) > 0.5){
            asset.changeHypeLevel(SemiRandomValuesGenerator.getFromNormal(-amount / 10, 5));
        }
        asset.changeAmountOfOwners(posses(asset) - 1);

    }

    public void fillGridPane(GridPane traderDetails){
        String [] filledDetails = {tradingIdentifier, type, name, isBear.toString()};
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            traderDetails.add(l, 0, i);
        }
    }
    public int posses(Asset asset){
        if(Objects.isNull(investmentBudget.get(asset.getName()))){
            return 0;
        }
        return 1;
    }
    public int notPosses(Asset asset){
        if(Objects.isNull(investmentBudget.get(asset.getName()))){
            return 1;
        }
        return 0;
    }

    @Override
    public void run() {
        while(App.isRunning){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            //Now perform some random buing/selling/increasing budget with some probability
            float transactionProbability = world.getTransactionProbability();
            if (isBear) transactionProbability += 0.1;
            else transactionProbability -= 0.1;
            
            if(investmentBudget.isEmpty() || SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.2){
                increaseBudget(); 
            }
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability){
                tradeOnMarket("buy");
            }
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability){
                tradeOnMarket("sell");
            }
        }
        //System.out.println("Thread is closing!");
       
    }
}
