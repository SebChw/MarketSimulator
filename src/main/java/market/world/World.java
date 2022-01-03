package market.world;

import market.traders.*;
import market.assets.*;
import market.assets.marketIndex.*;
import market.markets.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import market.entityCreator.EntityFactory;
import market.entityCreator.SemiRandomValuesGenerator;
import market.exchangeRates.ExchangeRatesProvider;
import market.gui.asset.AssetViewController;

public class World {
    private ArrayList<Company> companies = new ArrayList<Company>();
    private ArrayList<HumanInvestor> humanInvestors = new ArrayList<HumanInvestor>();
    private ArrayList<InvestmentFund> investmentFunds = new ArrayList<InvestmentFund>();
    private ArrayList<Trader> allTraders = new ArrayList<Trader>();
    private ArrayList<Market> currencyMarkets = new ArrayList<Market>();
    private ArrayList<Market> commodityMarkets = new ArrayList<Market>();
    private ArrayList<Market> stockMarkets = new ArrayList<Market>();
    private ArrayList<Market> allMarkets = new ArrayList<Market>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();
    private ArrayList<Commodity> commodities = new ArrayList<Commodity>();
    private ArrayList<Share> shares = new ArrayList<Share>();
    private ArrayList<InvestmentFundUnit> fundUnits = new ArrayList<InvestmentFundUnit>();
    private HashMap<String, Asset> allAssets = new HashMap<String, Asset>();
    private ArrayList<MarketIndex> marketIndices = new ArrayList<MarketIndex>();
    private ArrayList<DynamicMarketIndex> dynamicMarketIndices = new ArrayList<DynamicMarketIndex>();
    //private ArrayList<Asset> allAssets = new ArrayList<Asset>();
    private EntityFactory entityFactory;

    //private ArrayList<ExchangeRatesProvider> exchangeRates = new ArrayList<ExchangeRatesProvider>();
    //private ExchangeRatesProvider mainBankRates = new ExchangeRatesProvider("Spice"); // WE Live in a beautifull world when everything is backed in Gold!
    private String mainAsset;

    private int numberOfCompanies = 0; // number of companies == number of shares!
    private int numberOfHumanInvestors = 0;
    private int numberOfInvestmentFunds = 0;
    private int numberOfMarkets = 0;
    private int numberOfCurrencies = 0;
    private int numberOfCommodities = 0;
    private int numberOfIndices = 0;
    private int numberOfInvestmentFundUnits = 0;
    private int numberOfStaticIndices = 0;
    private int numberOfDynamicIndices = 0;

    private Random randomGenerator = new Random();

    public int getNumberOfCompanies() {
        return this.numberOfCompanies;
    }

    public int getNumberOfHumanInvestors() {
        return this.numberOfHumanInvestors;
    }

    public int getNumberOfInvestmentFunds() {
        return this.numberOfInvestmentFunds;
    }

    public int getNumberOfMarkets() {
        return this.numberOfMarkets;
    }

    public int getNumberOfCurrencies() {
        return this.numberOfCurrencies;
    }

    public int getNumberOfCommodities() {
        return this.numberOfCommodities;
    }

    public int getNumberOfStaticIndices(){
        return this.numberOfStaticIndices;
    }

    public int getNumberOfDynamicIndices(){
        return this.numberOfDynamicIndices;
    }
    public World(String mainAsset){
        this.mainAsset = mainAsset;
        this.entityFactory = new EntityFactory(mainAsset);
    }

    public void addNewCurrency(){
        Currency cur = this.entityFactory.createCurrency();
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
        numberOfCurrencies+=1;
        addAssetToMarkets(cur);
    }

    public void addNewCurrency(Currency cur){
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
        numberOfCurrencies+=1;
    }

    public void addNewCommodity(){
        Commodity com = this.entityFactory.createCommodity(this.currencies);
        this.commodities.add(com);
        this.allAssets.put(com.getName(),com);
        numberOfCommodities +=1;
        addAssetToMarkets(com);
    }

    public void addNewCommodity(Commodity com){
        this.commodities.add(com);
        this.allAssets.put(com.getName(), com);
        numberOfCommodities+=1;
    }

    public void addAssetToMarkets(Asset asset){
        for (Market market : this.commodityMarkets) {
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.8){
                market.addNewAsset(asset);
            }
        }
    }
    public void addNewCompany(){
        //In that moment we should create Share object too!
        if (numberOfCurrencies > 0) {
            Company company = this.entityFactory.createCompany(this.currencies);
            this.companies.add(company);
            this.allTraders.add(company);
            numberOfCompanies += 1;
            Share share = company.getShare();
            this.shares.add(share);
            this.allAssets.put(share.getName(), share);
            addAssetToMarkets(share);

            for (DynamicMarketIndex index : this.dynamicMarketIndices) {
                index.update(company);
                index.updateIndex();
            }
        }
        else{
            System.out.println("Can't create Company withouth any Currencies!");
        }
        
    }

    public void addNewHumanInvestor(){
        HumanInvestor human = this.entityFactory.createHumanInvestor(this.currencies);
        this.humanInvestors.add(human);
        this.allTraders.add(human);
        numberOfHumanInvestors+=1;
    }

    public void addNewInvestmentFund(){
        if (numberOfCurrencies > 0) {
            InvestmentFund fund = this.entityFactory.createInvestmentFund(this.currencies);
            this.investmentFunds.add(fund);
            this.allTraders.add(fund);
            numberOfInvestmentFunds += 1;
        }
        else{
            System.out.println("Can't create Investment Fund withouth any Currencies!");
        }
    }
    public void addNewRandomMarket(){
        if (numberOfCurrencies > 0){
            float uniformNumber = randomGenerator.nextFloat();

            if (uniformNumber < 0.33){
                Market market = this.entityFactory.createMarket(this.currencies, this.currencies, null);
                market.setAssetType(this.currencies.get(0).getType());
                this.currencyMarkets.add(market);
                this.allMarkets.add(market);
            }
            else if (uniformNumber < 0.665){
                Market market = this.entityFactory.createMarket(this.currencies, this.commodities, null);
                market.setAssetType(this.commodities.get(0).getType());
                this.commodityMarkets.add(market);
                this.allMarkets.add(market);
            }
            else {
                ArrayList<Asset> allIndices = new ArrayList<Asset>();
                allIndices.addAll(this.marketIndices);
                allIndices.addAll(this.dynamicMarketIndices);
                Market market = this.entityFactory.createMarket(this.currencies, this.shares, allIndices);
                market.setAssetType(this.shares.get(0).getType());
                this.stockMarkets.add(market);
                this.allMarkets.add(market);
            }
            
            numberOfMarkets += 1;

        }
        else {
            System.out.println("You can't create Market Withouth any currency!");
        }

    }

    public void addNewMarketIndex(){
        if (this.companies.isEmpty()){
            System.out.println("You can'y create market Index with no companies in the world!");
            return;
        }
        MarketIndex marketIndex = this.entityFactory.createMarketIndex(this.companies);
        this.marketIndices.add(marketIndex);
        this.allAssets.put(marketIndex.getName(), marketIndex);
        this.numberOfStaticIndices += 1;
    }

    public void addNewDynamicMarketIndex(){
        if (this.companies.isEmpty()){
            System.out.println("You can't create market Index with no companies in the world!");
            return;
        }

        String type = "biggest";
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5) type = "startup";

        DynamicMarketIndex marketIndex = this.entityFactory.createDynamicMarketIndex(this.companies, type);
        this.dynamicMarketIndices.add(marketIndex);
        this.allAssets.put(marketIndex.getName(), marketIndex);
        this.numberOfDynamicIndices += 1;

    }

    public void addNewInvestmentFundUnit(InvestmentFund issuedBy){
        //This method won't be executed very often so thaat I can create new ArrayList each time
        if (numberOfCurrencies + numberOfCommodities + numberOfCompanies > 0){
            InvestmentFundUnit unit = this.entityFactory.createFundUnit(issuedBy, new ArrayList<>(this.allAssets.values()));
            this.fundUnits.add(unit);
            this.allAssets.put(unit.getName(), unit);
            numberOfInvestmentFundUnits +=1;

        }
        else {
            System.out.println("You can't create FundUnit withouth any Assets on the Market");
        }
        
    }
    // public void addNewCurrencyMarket(){
    //     //!Maybe create just one function that with some probability creates one of these markets
    //     if (numberOfCurrencies > 0){
    //         //compiler inferred that he need to put type currency. LOL!
    //         this.currencyMarkets.add(this.entityFactory.createMarket(this.currencies, false));
    //         numberOfMarkets += 1;
    //     }
    //     else{
    //         System.out.println("You can't create Market Withouth any currency!");
    //     }
        
    // }

    // public void addNewCommodityMarket(){
    //     if (numberOfCurrencies > 0){
    //         this.commodityMarkets.add(this.entityFactory.createMarket(this.currencies, false));
    //         numberOfMarkets += 1;
    //     }
    //     else{
            
    //         System.out.println("You can't create Market Withouth any currency!");
    //     }
    // }

    // public void addNewStockMarket(){
    //     if (numberOfCurrencies > 0){
    //     this.stockMarkets.add(this.entityFactory.createMarket(this.currencies, false));
    //     numberOfMarkets += 1;
    //     }
    //     else{
    //         System.out.println("You can't create Market Withouth any currency!");
    //     }
        
    // }

    public ArrayList<Asset> getAllAssets(){
        //This method won't be executed very often so thaat I can create new ArrayList each time
        return new ArrayList<>(this.allAssets.values());
    }
    public ArrayList<Market> getAllMarkets(){
        return this.allMarkets;
    }
    public ArrayList<Trader> getAllTraders(){
        return this.allTraders;
    }

    public Asset getParticularAsset(String name){
        //This method Will be executed veeeery often that's Why I should provide O(1) lookup
        return this.allAssets.get(name);
    }
  
}
