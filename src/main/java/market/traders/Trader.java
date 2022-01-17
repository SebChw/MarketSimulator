package market.traders;

import market.markets.Market;
import market.world.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.control.Label;
import market.AppInitializer;
import market.assets.Asset;
import market.assets.Currency;
import market.entityCreator.SemiRandomValuesGenerator;
import market.interfaces.Dealer;
import market.interfaces.Searchable;

/**
 * Base class for all types of object that can perform some trading on a market.
 */
public class Trader implements Runnable, Searchable, Serializable {
    private String tradingIdentifier;
    private HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
    private String name;
    private String type;
    private transient SimpleFloatProperty budgetInGold = new SimpleFloatProperty(0);
    private String[] details = { "Id: ", "Type: ", "Name: ", "Is Bear: " };
    Boolean isBear;
    private World world;

    private ArrayList<Float> properties = new ArrayList<Float>();

    /**
     * 
     * @param tradingIdentifier this identifier should uniquely identify the trader
     *                          in the world
     * @param investmentBudget  Starting budget of a trader
     * @param name              name on a trader
     * @param type              type of a trader. I thought about using
     *                          this.getClass().getName() instead of this but I was
     *                          not sure.
     * @param isBear            boolean that influences the transaction probability
     *                          of a trader
     * @param world             world on which trader lives
     */
    public Trader(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name, String type,
            boolean isBear, World world) {
        this.tradingIdentifier = tradingIdentifier;
        this.investmentBudget = investmentBudget;
        this.name = name;
        this.type = type;
        this.isBear = isBear;
        this.world = world;

        calculateBudgetInGold();
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return this.tradingIdentifier + " " + this.type + " " + this.name;
    }

    /**
     * @return World
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Function which select random market from the world, then selects random asset
     * and random amount of it and then ask market for transaction
     * 
     * @param operation can be "buy" or "sell"
     */
    public void tradeOn(String operation, Dealer dealer) {

        if (operation == "buy") {
            ArrayList<Asset> availableAssets = dealer.getAvailableAssets();
            Asset chosenAsset = availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets));
            dealer.buy(this, chosenAsset, chosenAsset.getPossibleAmount());
        } else {
            List<Asset> availableAssets = dealer.getAvailableAssets().stream()
                    .filter(a -> this.investmentBudget.containsKey(a.getName())).collect(Collectors.toList());
            if (!availableAssets.isEmpty()) {
                Asset chosenAsset = availableAssets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableAssets));

                float chosenAmount = chosenAsset.getPossibleAmount(investmentBudget.get(chosenAsset.getName()));
                dealer.sell(this, chosenAsset, chosenAmount);
            }
        }
    }

    public void tradeOnMarket(String operation) {
        if (!world.getWorldContainer().getAllMarkets().isEmpty()) {
            ArrayList<Market> availableMarkets = world.getWorldContainer().getAllMarkets();
            Market market = availableMarkets.get(SemiRandomValuesGenerator.getRandomArrayIndex(availableMarkets));
            tradeOn(operation, market);
        }
    }

    /**
     * Function adding random amount of randomly selected currency to the Investor's
     * budget
     */
    public void increaseBudget() {
        ArrayList<Currency> availableCurrencies = world.getWorldContainer().getCurrencies();
        Currency randomCurrency = availableCurrencies
                .get(SemiRandomValuesGenerator.getRandomArrayIndex(availableCurrencies));

        this.addBudget(randomCurrency, 1000);
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return String
     */
    public String getTradingIdentifier() {
        return this.tradingIdentifier;
    }

    /**
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return SimpleFloatProperty
     */
    public SimpleFloatProperty budgetInGoldProperty() {
        return this.budgetInGold;
    }

    /**
     * Function iterating over investor's budget and calculating it's budget in Gold
     * (main asset).
     */
    public void calculateBudgetInGold() {
        if (Objects.isNull(investmentBudget)) {
            this.budgetInGold.set(0);
            return;
        }

        float budgetInGold = 0;
        Iterator<Map.Entry<String, Float>> it = investmentBudget.entrySet().iterator();
        Map.Entry<String, Float> budget = null;
        while (it.hasNext()) {
            budget = it.next();
            budgetInGold += (world.getParticularAsset(budget.getKey())
                    .calculateThisToMain(budget.getValue()));
        }
        this.budgetInGold.set(budgetInGold);
    }

    /**
     * 
     * @param asset  to get correct rate
     * @param amount how much of this asset we obtained/lost
     */
    public void changeBudgetInGold(Asset asset, float amount) {
        this.budgetInGold.set(this.budgetInGold.get() + asset.calculateThisToMain(amount));
    }

    /**
     * @return HashMap<String, Float>
     */
    public HashMap<String, Float> getInvestmentBudget() {
        return this.investmentBudget;
    }

    /**
     * Function that first change parameters of asset (amount In Circulation, hype,
     * # of owners) -> Increase amount in Investors budget ->
     * Updates investor's budget in gold.
     * 
     * @param asset  which asset we want to change in budget
     * @param amount how much we want to add to the budget
     */
    public void addBudget(Asset asset, float amount) {
        if (amount < 0.1)
            return;
        // !I'm updating all assset information here
        asset.changeAmountInCirculation(amount);
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) > 0.5) {
            asset.changeHypeLevel(SemiRandomValuesGenerator.getFromNormal(amount / 10, 5));
        }
        asset.changeAmountOfOwners(notPosses(asset));

        String assetName = asset.getName();
        float currentBudget = this.investmentBudget.getOrDefault(assetName, (float) 0);
        investmentBudget.put(assetName, currentBudget + amount);
        changeBudgetInGold(asset, amount);

    }

    /**
     * Function that first change parameters of asset (amount In Circulation, hype,
     * # of owners) -> Decrease amount in Investors budget (To somehow prevent some
     * very small amount if amount decreases below 0.1 it is removed from the
     * budget) -> Updates investor's budget in gold.
     * 
     * @param asset
     * @param amount
     */
    public void subtractBudget(Asset asset, float amount) {
        String assetName = asset.getName();
        // !Throw exception here!
        float currentBudget = this.investmentBudget.get(assetName);
        if (Objects.isNull(currentBudget) || currentBudget < amount) {
            System.out.println("Logic Error!");
        } else {
            currentBudget -= amount;
            if (currentBudget < 0.1) {
                amount += currentBudget; // I should remove what I'm left with also
                this.investmentBudget.remove(assetName);
            } else {
                this.investmentBudget.put(assetName, currentBudget);
            }
        }
        // Updates asset parameters influencing it's price
        asset.changeAmountInCirculation(-amount);
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) > 0.5) {
            asset.changeHypeLevel(SemiRandomValuesGenerator.getFromNormal(-amount / 10, 5));
        }
        asset.changeAmountOfOwners(posses(asset) - 1);

        changeBudgetInGold(asset, -amount); // Decreases budget in gold

    }

    /**
     * Automatically fill given GridPane with labels containing details of the
     * Trader
     * 
     * @param traderDetails
     */
    public void fillGridPane(GridPane traderDetails) {
        String[] filledDetails = { tradingIdentifier, type, name, isBear.toString() };
        addLabelsToPane(traderDetails, this.details, filledDetails, 0);
    }

    /**
     * Function that add how many labels to the GridPane how many details it
     * has.
     * 
     * @param traderDetails
     * @param filledDetails
     * @param offset        from which cell in gridPane to start
     */
    public void addLabelsToPane(GridPane traderDetails, String[] details, String[] filledDetails, int offset) {
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            traderDetails.add(l, 0, i + offset);
        }
    }

    /**
     * Checks whether trader posses some asset
     * 
     * @param asset
     * @return int basically boolean value 0 or 1
     */
    public int posses(Asset asset) {
        if (Objects.isNull(investmentBudget.get(asset.getName()))) {
            return 0;
        }
        return 1;
    }

    /**
     * Check whether trader does not posses some asset
     * 
     * @param asset
     * @return int
     */
    public int notPosses(Asset asset) {
        if (Objects.isNull(investmentBudget.get(asset.getName()))) {
            return 1;
        }
        return 0;
    }

    /**
     * By operation I mean set of things that thread performs in one unit of time.
     * Then it sleeps and perform operation again.
     */
    public void operation() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(
                    "Thread was interrupted, stopping!");
        }

        // Now perform some random buing/selling/increasing budget with some probability
        float transactionProbability = world.getTransactionProbability();
        if (isBear)
            transactionProbability += 0.1;
        else
            transactionProbability -= 0.1;

        if (investmentBudget.isEmpty() || SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.2) {
            increaseBudget();
        }
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability) {
            tradeOnMarket("buy");
        }
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < transactionProbability) {
            tradeOnMarket("sell");
        }

    }

    @Override
    public void run() {
        while (AppInitializer.isRunning) {
            operation();
        }
        System.out.println("Thread: " + getName() + " Has done it's job and is being removed!");
        world.getWorldContainer().getAllThreads().remove(getName());

    }

    @Override
    public String getSearchString() {
        return name + type + tradingIdentifier;
    }

    /**
     * Writes all not serializable objects to serializable ones
     */
    public void setProperties() {
        properties.add(budgetInGold.get());
    }

    /**
     * Write previosult saved parameters of serializable objects
     */
    public void readProperties() {
        budgetInGold = new SimpleFloatProperty(properties.get(0));
    }
}
