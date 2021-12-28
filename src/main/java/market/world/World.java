package market.world;

import market.traders.*;
import market.assets.*;
import market.markets.*;
import java.util.ArrayList;
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
    private ArrayList<Asset> allAssets = new ArrayList<Asset>();
    private ArrayList<ExchangeRatesProvider> exchangeRates = new ArrayList<ExchangeRatesProvider>();;
    private EntityFactory entityFactory = new EntityFactory();


    private int numberOfCompanies = 0;
    private int numberOfHumanInvestors = 0;
    private int numberOfInvestmentFunds = 0;
    private int numberOfMarkets = 0;
    private int numberOfCurrencies = 0;
    private int numberOfCommodities = 0;
    private int numberOfIndices = 0;

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
        Currency cur = this.entityFactory.createCurrency(this.exchangeRates);
        this.currencies.add(cur);
        this.allAssets.add(cur);
        numberOfCurrencies+=1;
    }

    public void addNewCommodity(){
        Commodity com = this.entityFactory.createCommodity(this.currencies);
        this.commodities.add(com);
        this.allAssets.add(com);
        numberOfCommodities +=1;
    }

    public void addNewCompany(){
        Company company = this.entityFactory.createCompany(this.currencies);
        this.companies.add(company);
        this.allTraders.add(company);
        numberOfCompanies += 1;
    }

    public void addNewHumanInvestor(){
        HumanInvestor human = this.entityFactory.createHumanInvestor(this.currencies);
        this.humanInvestors.add(human);
        this.allTraders.add(human);
        numberOfHumanInvestors+=1;
    }

    public void addNewInvestmentFund(){
        InvestmentFund fund = this.entityFactory.createInvestmentFund(this.currencies);
        this.investmentFunds.add(fund);
        this.allTraders.add(fund);
        numberOfInvestmentFunds += 1;
    }
    public void addNewRandomMarket(){
        if (numberOfCurrencies > 0){
            float uniformNumber = randomGenerator.nextFloat();

            if (uniformNumber < 0.33){
                Market<Currency> market = this.entityFactory.createMarket(this.currencies, false);
                market.setAssetType("Currency");
                this.currencyMarkets.add(market);
                this.allMarkets.add(market);
            }
            else if (uniformNumber < 0.665){
                Market<Commodity> market = this.entityFactory.createMarket(this.currencies, false);
                market.setAssetType("Commodity");
                this.commodityMarkets.add(market);
                this.allMarkets.add(market);
            }
            else {
                Market<Share> market = this.entityFactory.createMarket(this.currencies, false);
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
        return this.allAssets;
    }
    public ArrayList<Market<?>> getAllMarkets(){
        return this.allMarkets;
    }
    public ArrayList<Trader> getAllTraders(){
        return this.allTraders;
    }

    
}
