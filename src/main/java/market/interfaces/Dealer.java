package market.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import market.assets.Asset;
import market.assets.Currency;
import market.traders.Trader;

public interface Dealer {
    public HashMap<String, Asset> getAvailableAssetsHashMap();

    public float getPercentageOperationCost();

    public Currency getTradingCurrency();

    public ArrayList<Asset> getAvailableAssets();

    public void buy(Trader trader, Asset wantedUnit, float amount);

    public void sell(Trader trader, Asset soldUnit, float amount);
}
