package market.world;

import market.traders.*;
import market.assets.*;
import market.markets.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import market.entityCreator.EntityFactory;
import market.exchangeRates.ExchangeRatesProvider;
import market.gui.asset.AssetViewController;

public class World {
    private ArrayList<Company> companies = new ArrayList<Company>();
    private ArrayList<HumanInvestor> humanInvestors = new ArrayList<HumanInvestor>();
    private ArrayList<InvestmentFund> investmentFunds = new ArrayList<InvestmentFund>();
    private ArrayList<Trader> allTraders = new ArrayList<Trader>();
    private ArrayList<Market<Currency>> currencyMarkets = new ArrayList<Market<Currency>>();
    private ArrayList<Market<Commodity>> commodityMarkets = new ArrayList<Market<Commodity>>();
    private ArrayList<Market<Share>> stockMarkets = new ArrayList<Market<Share>>();
    private ArrayList<Market<? extends Asset>> allMarkets = new ArrayList<Market<? extends Asset>>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();
    private ArrayList<Commodity> commodities = new ArrayList<Commodity>();
    private ArrayList<Share> shares = new ArrayList<Share>();
    private ArrayList<InvestmentFundUnit> fundUnits = new ArrayList<InvestmentFundUnit>();
    private HashMap<String, Asset> allAssets = new HashMap<String, Asset>(); 
    //private ArrayList<Asset> allAssets = new ArrayList<Asset>();
    private EntityFactory entityFactory = new EntityFactory();

    //private ArrayList<ExchangeRatesProvider> exchangeRates = new ArrayList<ExchangeRatesProvider>();
    private ExchangeRatesProvider mainBankRates = new ExchangeRatesProvider("Spice"); // WE Live in a beautifull world when everything is backed in Gold!


    private int numberOfCompanies = 0; // number of companies == number of shares!
    private int numberOfHumanInvestors = 0;
    private int numberOfInvestmentFunds = 0;
    private int numberOfMarkets = 0;
    private int numberOfCurrencies = 0;
    private int numberOfCommodities = 0;
    private int numberOfIndices = 0;
    private int numberOfInvestmentFundUnits = 0;

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

    public int getNumberOfIndices(){
        return this.numberOfIndices;
    }
    public World(){
    }

    public void addNewCurrency(){
        Currency cur = this.entityFactory.createCurrency(this.mainBankRates);
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
        numberOfCurrencies+=1;
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
    }

    public void addNewCommodity(Commodity com){
        this.commodities.add(com);
        this.allAssets.put(com.getName(), com);
        numberOfCommodities+=1;
    }

    public void addNewCompany(){
        //In that moment we should create Share object too!
        if (numberOfCurrencies > 0) {
            Company company = this.entityFactory.createCompany(this.currencies);
            this.companies.add(company);
            this.allTraders.add(company);
            numberOfCompanies += 1;
            Share share = this.entityFactory.createShare(company);
            this.shares.add(share);
            this.allAssets.put(share.getName(), share);
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
                Market<Currency> market = this.entityFactory.createMarket(this.currencies, false, this.currencies);
                market.setAssetType("Currency");
                this.currencyMarkets.add(market);
                this.allMarkets.add(market);
            }
            else if (uniformNumber < 0.665){
                Market<Commodity> market = this.entityFactory.createMarket(this.currencies, false, this.commodities);
                market.setAssetType("Commodity");
                this.commodityMarkets.add(market);
                this.allMarkets.add(market);
            }
            else {
                Market<Share> market = this.entityFactory.createMarket(this.currencies, false, this.shares);
                market.setAssetType("Stock");
                this.stockMarkets.add(market);
                this.allMarkets.add(market);
            }
            
            numberOfMarkets += 1;

        }
        else {
            System.out.println("You can't create Market Withouth any currency!");
        }

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
    public void testTrade(){
        Market<?> market = this.allMarkets.get(0);
        Trader trader = this.allTraders.get(0);
        HashMap<String, ? extends Asset> allAssets = market.getAvailableAssets();
        String [] allAssetsNames = allAssets.keySet().toArray(new String[0]);
        String asset = allAssetsNames[0];

        //market.buy(trader, asset, 1);
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
    public ArrayList<Market<?>> getAllMarkets(){
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
