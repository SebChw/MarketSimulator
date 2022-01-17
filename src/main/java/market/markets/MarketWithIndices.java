package market.markets;

import java.util.HashMap;

import market.assets.Asset;
import market.assets.Currency;
import market.world.Constants;
import market.world.World;

/**
 * If market is with indices we can additionally buy Indices on it. On Simple
 * market it is not possible!
 */
public class MarketWithIndices extends Market {

    private String[] availableIndices = { Constants.indexType, Constants.dynamicIndexType };

    public MarketWithIndices(String name, String country, String city, String address, float percentageOperationCost,
            Currency tradingCurrency, HashMap<String, Asset> availableAssets, World world) {
        super(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets, world);

    }

    /**
     * To this market we can add assets defined earlier like
     * Currency/Commodity/Shares and additionally indices
     * 
     * @param asset
     */
    @Override
    public void addNewAsset(Asset asset) {
        super.addNewAsset(asset);
        if (checkMarketIndexType(asset)) {
            this.getAvailableAssetsHashMap().put(asset.getName(), asset);
        }
    }

    private boolean checkMarketIndexType(Asset marketIndex) {
        String type = marketIndex.getType();
        for (String available : availableIndices) {
            if (available.equals(type))
                return true;
        }
        return false;
    }
}
