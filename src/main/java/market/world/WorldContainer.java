package market.world;

import market.traders.*;
import market.assets.*;
import market.assets.marketIndex.*;
import market.markets.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Container for all objects stored in the world
 */
public class WorldContainer implements Serializable {
    private ArrayList<Company> companies = new ArrayList<Company>();
    private ArrayList<InvestmentFund> investmentFunds = new ArrayList<InvestmentFund>();
    private ArrayList<Trader> allTraders = new ArrayList<Trader>();
    private ArrayList<Market> allMarkets = new ArrayList<Market>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();
    private ArrayList<Commodity> commodities = new ArrayList<Commodity>();
    private ArrayList<Share> shares = new ArrayList<Share>();
    private HashMap<String, Asset> allAssets = new HashMap<String, Asset>();
    private ArrayList<MarketIndex> marketIndices = new ArrayList<MarketIndex>();
    private ArrayList<DynamicMarketIndex> dynamicMarketIndices = new ArrayList<DynamicMarketIndex>();
    private HashMap<String, Thread> allThreads = new HashMap<String, Thread>();

    /**
     * Add given currency to the world
     * 
     * @param cur
     */
    public void addNewCurrency(Currency cur) {
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
    }

    /**
     * @param share
     */
    public void addNewShare(Share share) {
        this.allAssets.put(share.getName(), share);
        this.shares.add(share);
    }

    /**
     * Add given commodity to the world
     * 
     * @param com
     */
    public void addNewCommodity(Commodity com) {
        this.commodities.add(com);
        this.allAssets.put(com.getName(), com);
    }

    public void addNewCompany(Company comp) {
        this.companies.add(comp);
        this.allTraders.add(comp);
    }

    public void addNewHumanInvestor(HumanInvestor human) {
        allTraders.add(human);
    }

    public void addNewInvestmentFund(InvestmentFund fund) {
        investmentFunds.add(fund);
        allTraders.add(fund);
    }

    public void addNewMarket(Market market) {
        allMarkets.add(market);
    }

    public void addNewInvestmentFundUnit(InvestmentFundUnit unit) {
        allAssets.put(unit.getName(), unit);
    }

    public void addNewMarketIndex(MarketIndex index) {
        marketIndices.add(index);
        allAssets.put(index.getName(), index);
    }

    public void addNewDynamicMarketIndex(DynamicMarketIndex index) {
        dynamicMarketIndices.add(index);
        allAssets.put(index.getName(), index);
    }

    public void addNewThread(String name, Thread thread) {
        allThreads.put(name, thread);
    }

    public HashMap<String, Thread> getAllThreads() {
        return allThreads;
    }

    /**
     * @return ArrayList<InvestmentFund>
     */
    public ArrayList<InvestmentFund> getInvestmentFunds() {
        return this.investmentFunds;
    }

    /**
     * @return ArrayList<Asset>
     */
    public ArrayList<Asset> getAllAssets() {
        // This method won't be executed very often so thaat I can create new ArrayList
        // each time
        return new ArrayList<>(this.allAssets.values());
    }

    public HashMap<String, Asset> getAllAssetsHashMap() {
        // This method won't be executed very often so thaat I can create new ArrayList
        // each time
        return allAssets;
    }

    /**
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllAssetsNames() {
        return new ArrayList<String>(this.allAssets.keySet());
    }

    /**
     * @return ArrayList<Market>
     */
    public ArrayList<Market> getAllMarkets() {
        return this.allMarkets;
    }

    /**
     * @return ArrayList<Trader>
     */
    public ArrayList<Trader> getAllTraders() {
        return this.allTraders;
    }

    /**
     * @return ArrayList<Currency>
     */
    public ArrayList<Currency> getCurrencies() {
        return this.currencies;
    }

    public ArrayList<DynamicMarketIndex> getDynamicMarketIndices() {
        return dynamicMarketIndices;
    }

    public ArrayList<Share> getShares() {
        return shares;
    }

    public ArrayList<MarketIndex> getMarketIndices() {
        return marketIndices;
    }

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }
}
