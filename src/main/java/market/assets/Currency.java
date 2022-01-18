package market.assets;

import java.util.HashSet;

import market.world.Constants;
import market.priceRules.NoAmountInCirculationPriceRule;

/** Class representing Currency in our world. */
public class Currency extends Asset {
    private HashSet<String> countriesWhereLegal;

    public Currency(String name, HashSet<String> countriesWhereLegal, String backingAsset, float startingRate) {
        super(name, Constants.currencyType, 0, backingAsset, startingRate);
        this.countriesWhereLegal = countriesWhereLegal;

        if (startingRate != 0)
            getMainBankRates().updateRate(startingRate);

        getMainBankRates().setAssetPriceRule(new NoAmountInCirculationPriceRule());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\nLegal in: " + countriesWhereLegal;
    }
}
