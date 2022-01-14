package market.assets;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import market.exchangeRates.ExchangeRatesProvider;
import market.interfaces.Searchable;
import market.entityCreator.SemiRandomValuesGenerator;

/**
 * This is root class for all assets available in the market
 * 
 */
public abstract class Asset implements Searchable {
    private String name;
    private volatile SimpleFloatProperty amountInCirculation;
    private String type;
    private volatile SimpleFloatProperty hypeLevel = new SimpleFloatProperty(0);
    private volatile SimpleIntegerProperty amountOfOwners = new SimpleIntegerProperty(0);
    private ExchangeRatesProvider mainBankRates;

    public Asset(String name, String type, float amountInCirculation, String backingAsset, float startingRate) {
        this.name = name;
        this.type = type;
        this.amountInCirculation = new SimpleFloatProperty(amountInCirculation);
        this.mainBankRates = new ExchangeRatesProvider(backingAsset, startingRate);
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * returns "Commodity" or "Currency" etc.
     * 
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Return How many of Given asset is owned by the users
     * 
     * @return SimpleFloatProperty
     */
    public SimpleFloatProperty amountInCirculationProperty() {
        return this.amountInCirculation;
    }

    /**
     * How many traders owns that property
     * 
     * @return IntegerProperty
     */
    public IntegerProperty amountOfOwnersProperty() {
        return this.amountOfOwners;
    }

    /**
     * what is the hype of given property
     * 
     * @return SimpleFloatProperty
     */
    public SimpleFloatProperty hypeLevelProperty() {
        return this.hypeLevel;
    }

    /**
     * 
     * @param amount
     */
    public synchronized void changeAmountInCirculation(float amount) {
        float newValue = this.amountInCirculation.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.amountInCirculation.get(), "amount");
        this.amountInCirculation.set(newValue);
    }

    /**
     * @param amount
     */
    public synchronized void changeHypeLevel(float amount) {
        float newValue = this.hypeLevel.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.hypeLevel.get(), "hypeLevel");
        this.hypeLevel.set(newValue);

    }

    /**
     * @param amount
     */
    public synchronized void changeAmountOfOwners(int amount) {
        int newValue = this.amountOfOwners.get() + amount;
        this.mainBankRates.updateWRT(newValue - this.amountOfOwners.get(), "owners");
        this.amountOfOwners.set(newValue);

    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Minimal Price: " + mainBankRates.getMinPrice() +
                "\nMaximum Price: " + mainBankRates.getMaxPrice();
    }

    /**
     * Calculate how much main Currency we can get from this asset
     * 
     * @param amount
     * @return float
     */
    public float calculateThisToMain(float amount) {
        return 1 / this.mainBankRates.getRate() * amount;
    }

    /**
     * Calculates how many of this asset we can get for main Currency
     * 
     * @param amount
     * @return float
     */
    public float calculateMainToThis(float amount) {
        return this.mainBankRates.getRate() * amount;
    }

    /**
     * @return ExchangeRatesProvider
     */
    public ExchangeRatesProvider getMainBankRates() {
        return this.mainBankRates;
    }

    /**
     * @return float
     */
    public float getCurrentRate() {
        return this.mainBankRates.getRate();
    }

    public void updateRate() {
        mainBankRates.updateRate();
    }

    /**
     * Whether we can buy a particular asset
     * 
     * @param amount
     * @return boolean
     */
    public boolean canBeBought(float amount) {
        return true;
    }

    /**
     * If somehow our transaction ended in a halfway we can unfreeze the amount we
     * started buying
     * 
     * @param howMuch
     */
    public void unfreeze(float howMuch) {

    }

    /**
     * How much of particular asset we can buy at the moment
     * 
     * @return float
     */
    public float getPossibleAmount() {
        return 1000 * SemiRandomValuesGenerator.getRandomFloatNumber(1);
    }

    /**
     * @param amount
     * @return float
     */
    public float getPossibleAmount(float amount) {
        return amount * SemiRandomValuesGenerator.getRandomFloatNumber(1);
    }

    /**
     * Calculate how much of given asset we can get for particular amount of this
     * asset
     * 
     * @param tradedAsset
     * @param amount
     * @return float
     */
    public float calculateThisToDifferent(Asset tradedAsset, float amount) {
        // Here I could just do division However I think this is more clear?
        return tradedAsset.calculateMainToThis(this.calculateThisToMain(amount));
    };

    /**
     * Calculate how much of this asset we can get for given one
     * 
     * @param tradedAsset
     * @param amount
     * @return float
     */
    public float calculateDifferentToThis(Asset tradedAsset, float amount) {
        return this.calculateMainToThis(tradedAsset.calculateThisToMain(amount));
    };

    @Override
    public String getSearchString() {
        return name + type;
    }
}
