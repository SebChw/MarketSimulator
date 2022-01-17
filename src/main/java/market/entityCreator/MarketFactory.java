package market.entityCreator;

import market.world.World;
import market.assets.Currency;
import market.assets.Asset;
import market.markets.*;

import java.io.Serializable;
import java.util.*;

/**
 * Function for creating markets
 */
public class MarketFactory implements Serializable {
    private SemiRandomValuesGenerator attributesGenerator;

    private World world;

    public MarketFactory(World world) {
        this.world = world;
        this.attributesGenerator = new SemiRandomValuesGenerator(world);
    }

    /**
     * Generic function able to create market of any type
     * 
     * @param currenciesByNow to select some Currency available in market
     * @param assets          to select some assets available on market
     * @param indices         indices that can be sold on market if indices cant be
     *                        sold pass null
     * @return Market
     */
    public <T extends Asset> Market createMarket(ArrayList<Currency> currenciesByNow, ArrayList<T> assets,
            ArrayList<Asset> indices) {
        HashMap<String, String> attributes = this.attributesGenerator.getRandomMarketData();
        String name = attributes.get("name");
        String country = attributes.get("country");
        String city = attributes.get("city");
        String address = attributes.get("address");
        float percentageOperationCost = SemiRandomValuesGenerator.getRandomFloatNumber(0.1f, 0.1f);
        Currency tradingCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        HashMap<String, Asset> availableAssets = new HashMap<String, Asset>();

        // In general some Market may don't have all asssets from the very beginning.
        // However now I assume all have all assets!.
        for (Asset asset : assets) {
            availableAssets.put(asset.getName(), asset);

        }

        if (indices != null) {
            // In general some Market may don't have all asssets from the very beginning.
            // However now I assume all have all assets!.
            for (Asset index : indices) {
                availableAssets.put(index.getName(), index);
            }
            return new MarketWithIndices(name, country, city, address, percentageOperationCost, tradingCurrency,
                    availableAssets, this.world);
        } else {
            return new Market(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets,
                    this.world);
        }

    }
}
