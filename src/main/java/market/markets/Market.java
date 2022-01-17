package market.markets;

import market.assets.Currency;
import market.interfaces.Dealer;
import market.interfaces.Searchable;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.ArrayList;
import market.traders.Trader;
import market.transactions.*;
import market.world.Constants;
import market.world.World;
import market.assets.Asset;

/**
 * Market is a container for Assets that can be bought on it. Market add
 * additional cost to each transaction performed on it
 */
public class Market implements Searchable, Dealer, Serializable {
    private String name;
    private String country;
    private String city;
    private String address;
    private float percentageOperationCost;
    private Currency tradingCurrency;
    private String assetType;
    private TransactionSystem transactionSystem = new PositiveTransactionSystem();

    private String[] details = { "Name: ", "Country: ", "City: ", "Address: ", "Operation cost: ", "Trading Currency: ",
            "Asset type: " };

    private HashMap<String, Asset> availableAssets = new HashMap<String, Asset>(); // Maybe make this justa HashSet ?
    private World world;

    public Market(String name, String country, String city, String address,
            float percentageOperationCost, Currency tradingCurrency, HashMap<String, Asset> availableAssets,
            World world) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.percentageOperationCost = percentageOperationCost;
        this.tradingCurrency = tradingCurrency;
        this.availableAssets = availableAssets;
        this.world = world;

        this.assetType = initializeAssetType();

    }

    /**
     * @return String
     */
    public String toString() {
        return "Click me to see Available Assets!";
    }

    /**
     * Performs buyOperation on given TransactionSystem
     * 
     * @param trader
     * @param wantedAsset
     * @param amount
     */
    public void buy(Trader trader, Asset wantedAsset, float amount) {
        transactionSystem.buyOperation(this, trader, wantedAsset, amount, world);
    }

    /**
     * Performs sellOperation on given Transaction System
     * 
     * @param trader
     * @param soldAsset
     * @param amount
     */
    public void sell(Trader trader, Asset soldAsset, float amount) {
        transactionSystem.sellOperation(this, trader, soldAsset, amount, world);
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
    public String getAssetType() {
        return this.assetType;
    }

    /**
     * @param assetType
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return String
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * @return String
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return String
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @return float
     */
    public float getPercentageOperationCost() {
        return this.percentageOperationCost;
    }

    /**
     * @return String
     */
    public String getCurrencyName() {
        return this.tradingCurrency.getName();
    }

    /**
     * @return ArrayList<Asset>
     */
    public ArrayList<Asset> getAvailableAssets() {
        return new ArrayList<Asset>(this.availableAssets.values());
    }

    /**
     * @return HashMap<String, Asset>
     */
    public HashMap<String, Asset> getAvailableAssetsHashMap() {
        return this.availableAssets;
    }

    /**
     * @param asset
     */
    public void addNewAsset(Asset asset) {
        if (asset.getType().equals(this.assetType)) {
            this.availableAssets.put(asset.getName(), asset);
        }
    }

    /**
     * @param traderDetails
     */
    public void fillGridPane(GridPane traderDetails) {
        String[] filledDetails = { name, country, city, address,
                String.format("%.2f", percentageOperationCost * 100) + "%", tradingCurrency.getName(), assetType };
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            traderDetails.add(l, 0, i);
        }
    }

    @Override
    public String getSearchString() {
        return name + assetType + country + tradingCurrency.getName();
    }

    @Override
    public Currency getTradingCurrency() {
        return tradingCurrency;
    }

    public String initializeAssetType() {
        String type = world.getParticularAsset(availableAssets.keySet().iterator().next()).getType();
        if (type != Constants.currencyType && type != Constants.commodityType)
            type = Constants.shareType;

        return type;
    }

}
