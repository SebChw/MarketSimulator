package market.markets;

import java.util.HashMap;

import market.assets.Asset;
import market.assets.Currency;
import market.world.World;

public class MarketWithIndices extends Market{

    private String [] availableIndices = {"Market Index", "DynamicMarketIndex"};
    public MarketWithIndices(String name, String country, String city, String address, float percentageOperationCost,
            Currency tradingCurrency, HashMap<String, Asset> availableAssets, World world) {
        super(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets, world);


    }

    @Override
    public void addNewAsset(Asset asset){
        super.addNewAsset(asset);
        if (checkMarketIndexType(asset)){
            this.getAvailableAssetsHashMap().put(asset.getName(), asset);
        }
    }
   
    public boolean checkMarketIndexType(Asset marketIndex){
        String type = marketIndex.getType();
        for (String available : availableIndices){
            if (available.equals(type));
            return true;
        }
        return false;
    }
}
