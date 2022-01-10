package market.world;

import market.traders.*;
import market.assets.*;
import market.assets.marketIndex.*;
import market.markets.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import market.entityCreator.EntityFactory;
import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;

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


    private ObjectCounter objectCounter = new ObjectCounter();

    
    private volatile LocalDate currentTime = LocalDate.now(); // Update at constant intervals of time

    private  float transactionProbability = (float)0.5;
    private  float bullProbability = (float)0.5;

    //private ArrayList<Asset> allAssets = new ArrayList<Asset>();
    private EntityFactory entityFactory;

    //private ArrayList<ExchangeRatesProvider> exchangeRates = new ArrayList<ExchangeRatesProvider>();
    //private ExchangeRatesProvider mainBankRates = new ExchangeRatesProvider("Spice"); // WE Live in a beautifull world when everything is backed in Gold!
    private String mainAsset;



    private Random randomGenerator = new Random();

    public World(String mainAsset){
        this.mainAsset = mainAsset;
        this.entityFactory = new EntityFactory(mainAsset, this);
    }

    public void checkNeedOfCreatingInvestor(MainPanelController controller){
        //numberOfCompanies == numberOfShares
        float numberOfAssets = objectCounter.getAmountOfAssets();
        float numberOfInvestors = objectCounter.getAmountOfInvestors();
        if (numberOfInvestors/numberOfAssets < 0.25){
            float randomNumber = SemiRandomValuesGenerator.getRandomFloatNumber(1);
            if (randomNumber < 0.5) {
                controller.addHumanInvestor();
            }
            else {
                controller.addInvestmentFund();
            }
        }
    }

    public Currency addNewCurrency(){
        Currency cur = this.entityFactory.createCurrency();
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
        objectCounter.changeNumberOfCurrencies(1);
        addAssetToMarkets(cur);

        return cur;
    }

    public void addNewCurrency(Currency cur){
        this.currencies.add(cur);
        this.allAssets.put(cur.getName(), cur);
    }

    public Commodity addNewCommodity(){
        Commodity com = this.entityFactory.createCommodity(this.currencies);
        this.commodities.add(com);
        this.allAssets.put(com.getName(),com);
        objectCounter.changeNumberOfCommodities(1);
        addAssetToMarkets(com);

        return com;
    }

    public void addNewCommodity(Commodity com){
        this.commodities.add(com);
        this.allAssets.put(com.getName(), com);
    }

    public void addAssetToMarkets(Asset asset){ 
        for (Market market : getAllMarkets()) {
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.8){
                market.addNewAsset(asset);
            }
        }
    }

    
    public Company addNewCompany(){
        Company company = null;
        //In that moment we should create Share object too!
        if (objectCounter.getNumberOfCurrencies() > 0) {
            company = this.entityFactory.createCompany(this.currencies, this.dynamicMarketIndices);
            this.companies.add(company);
            this.allTraders.add(company);
            objectCounter.changeNumberOfCompanies(1);
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
        entityFactory.fillInitialBudgetRandomly(this.currencies, company);
        (new Thread(company)).start();
        return company;
    }

    public HumanInvestor addNewHumanInvestor(){
        HumanInvestor human = this.entityFactory.createHumanInvestor(this.currencies);
        this.humanInvestors.add(human);
        this.allTraders.add(human);
        objectCounter.changeNumberOfHumanInvestors(1);
        entityFactory.fillInitialBudgetRandomly(this.currencies, human);
        (new Thread(human)).start();
        return human;
    }

    public InvestmentFund addNewInvestmentFund(MainPanelController controller){
        InvestmentFund fund = null;
        if (objectCounter.getNumberOfCurrencies() > 0) {
            fund = this.entityFactory.createInvestmentFund(this.currencies, controller);
            this.investmentFunds.add(fund);
            this.allTraders.add(fund);
            objectCounter.changeNumberOfInvestmentFunds(1);
        }
        else{
            System.out.println("Can't create Investment Fund withouth any Currencies!");
        }
        entityFactory.fillInitialBudgetRandomly(this.currencies, fund);
        (new Thread(fund)).start();
        return fund;
    }
    public Market addNewRandomMarket(){
        Market market = null;
        if (objectCounter.getNumberOfCurrencies() > 0){
            float uniformNumber = randomGenerator.nextFloat();

            if (uniformNumber < 0.33){
                market = this.entityFactory.createMarket(this.currencies, this.currencies, null);
                this.currencyMarkets.add(market);
                this.allMarkets.add(market);
            }
            else if (uniformNumber < 0.665){
                market = this.entityFactory.createMarket(this.currencies, this.commodities, null);
                this.commodityMarkets.add(market);
                this.allMarkets.add(market);
            }
            else {
                ArrayList<Asset> allIndices = new ArrayList<Asset>();
                allIndices.addAll(this.marketIndices);
                allIndices.addAll(this.dynamicMarketIndices);
                market = this.entityFactory.createMarket(this.currencies, this.shares, allIndices);
                this.stockMarkets.add(market);
                this.allMarkets.add(market);
            }
            
            objectCounter.changeNumberOfMarkets(1);

        }
        else {
            System.out.println("You can't create Market Withouth any currency!");
        }
        return market;
    }

    public MarketIndex addNewMarketIndex(){
        MarketIndex marketIndex = null;
        if (this.companies.isEmpty()){
            System.out.println("You can'y create market Index with no companies in the world!");
            
        }
        marketIndex = this.entityFactory.createMarketIndex(this.companies);
        this.marketIndices.add(marketIndex);
        this.allAssets.put(marketIndex.getName(), marketIndex);
        objectCounter.changeNumberOfIndices(1);
        addAssetToMarkets(marketIndex);
        return marketIndex;
    }

    public DynamicMarketIndex addNewDynamicMarketIndex(){
        DynamicMarketIndex marketIndex = null;
        if (this.companies.isEmpty()){
            System.out.println("You can't create market Index with no companies in the world!");
            
        }

        String type = "biggest";
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5) type = "startup";

        marketIndex = this.entityFactory.createDynamicMarketIndex(this.companies, type);
        this.dynamicMarketIndices.add(marketIndex);
        this.allAssets.put(marketIndex.getName(), marketIndex);
        objectCounter.changeNumberOfDynamicIndices(1);
        addAssetToMarkets(marketIndex);
        return marketIndex;

    }

    public InvestmentFundUnit addNewInvestmentFundUnit(InvestmentFund issuedBy){
        //This method won't be executed very often so thaat I can create new ArrayList each time
        if (objectCounter.getAmountOfAssets() > 0){
            InvestmentFundUnit unit = this.entityFactory.createFundUnit(issuedBy, new ArrayList<>(this.allAssets.values()));
            this.fundUnits.add(unit);
            this.allAssets.put(unit.getName(), unit);
            objectCounter.changeNumberOfInvestmentFundUnits(1);
            issuedBy.getController().addInvestmentFundUnit(unit);
            return unit;
        }
        else {
            System.out.println("You can't create FundUnit withouth any Assets on the Market");
            return null;
        }
        
    }

    public ObjectCounter getObjectCounter(){
        return this.objectCounter;
    }
    public ArrayList<Asset> getAllAssets(){
        //This method won't be executed very often so thaat I can create new ArrayList each time
        return new ArrayList<>(this.allAssets.values());
    }
    public ArrayList<String> getAllAssetsNames(){
        return new ArrayList<String>(this.allAssets.keySet());
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

    public ArrayList<Currency> getCurrencies(){
        return this.currencies;
    }

    public synchronized void addOneDay() {
        currentTime = currentTime.plusDays(1);
    }

    public LocalDate getCurrentTime() {
        return currentTime;
    }
    
    public  float getTransactionProbability(){
        return transactionProbability;
    }
    public  void setTransactionProbability(float probability){
        transactionProbability = probability;
        System.out.println(transactionProbability);
    }

    public  float getBullProbability(){
        return bullProbability;
    }

    public  void setBullProbability(float probability){
        bullProbability = probability;
        System.out.println(bullProbability);
    }

    public void updateAllRates(){
        for (Asset asset : new ArrayList<>(this.allAssets.values())) {
            asset.updateRate();
            //asset.changeAmountOfOwners(1);
        }
    }
    public void updateAllIndices(){
        for (Company company : companies){
            company.notifyObservers();
        }
        for (DynamicMarketIndex index : this.dynamicMarketIndices) {
            index.updateIndex();
            //asset.changeAmountOfOwners(1);
        }
    }
    public void addNewShare(Share share){
        this.allAssets.put(share.getName(), share);
    }

    public ArrayList<InvestmentFund> getInvestmentFunds(){
        return this.investmentFunds;
    }
    
    public String getMainAsset(){
        return mainAsset;
    }
}
