package market.assets;

import java.util.HashSet;

/** Class representing Currency in our world. */
public class Currency extends Asset {
    private HashSet<String> countriesWhereLegal;

    public Currency(String name, HashSet<String> countriesWhereLegal, String backingAsset, float startingRate) {
        super(name, "Currency", 0, backingAsset, startingRate);
        this.countriesWhereLegal = countriesWhereLegal;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\nLegal in: " + countriesWhereLegal;
    }
}
