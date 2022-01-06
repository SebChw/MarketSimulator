package market.entityCreator;

import market.traders.*;
import market.world.World;
import market.assets.Currency;
import market.assets.Asset;
import market.assets.Commodity;
import market.assets.InvestmentFundUnit;
import market.markets.*;
import java.util.*;
import java.util.random.RandomGenerator;
import market.assets.marketIndex.MarketIndex;
import market.exchangeRates.ExchangeRatesProvider;
import market.assets.marketIndex.MarketIndex;

import market.assets.marketIndex.*;

public class EntityFactory {
    private SemiRandomValuesGenerator attributesGenerator; 
    private String mainAsset;

    //We can select it randomly
    private CompaniesFilter biggestFilter = new BiggestCompaniesFilter();
    private CompaniesFilter startupsFilter = new StartupsCompaniesFilter();
    private World world;
    public EntityFactory(String mainAsset, World world){
       this.mainAsset = mainAsset;
       this.world = world ;
       this.attributesGenerator = new SemiRandomValuesGenerator(world); 
    }

    public Company createCompany(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> company = this.attributesGenerator.getRandomCompanyData();
        String tradingIdentifier = company.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = company.get("name");
        String ipoDate = company.get("ipoDate");
        Float ipoShareValue = SemiRandomValuesGenerator.getRandomFloatNumber(5000, 0.1f) + 100;
        float openingPrice = SemiRandomValuesGenerator.getRandomFloatNumber(10000, 0.1f) + 100;
        float profit = SemiRandomValuesGenerator.getRandomFloatNumber(10000, 0.1f) + 100;
        float revenue = SemiRandomValuesGenerator.getRandomFloatNumber(5000, 0.1f) + 100;
        boolean isBear = attributesGenerator.getBearIndicator();
        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new Company(tradingIdentifier, investmentBudget, name, ipoDate, ipoShareValue, openingPrice, profit, revenue, registeredCurrency, isBear, this.world);
    }

    public HumanInvestor createHumanInvestor(ArrayList<Currency> currenciesByNow) {
        HashMap<String, String> human = this.attributesGenerator.getRandomHumanInvestorData();
        String tradingIdentifier = human.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = human.get("name");
        String lastName = human.get("lastName");
        boolean isBear = attributesGenerator.getBearIndicator();

        return new HumanInvestor(tradingIdentifier, investmentBudget, name, lastName, isBear, this.world);
    }

    public InvestmentFund createInvestmentFund(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> investmentFund = this.attributesGenerator.getRandomInvestmentFundData();
        String tradingIdentifier = investmentFund.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = investmentFund.get("name");
        String menagerFirstName = investmentFund.get("menagerFirstName");
        String menagerLastName = investmentFund.get("menagerLastName");
        boolean isBear = attributesGenerator.getBearIndicator();
        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new InvestmentFund(tradingIdentifier, investmentBudget, name, menagerFirstName, menagerLastName, registeredCurrency, isBear, this.world);
    }

    public Commodity createCommodity(ArrayList<Currency> currenciesByNow){
        //Drowing some random attributes
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCommodityData();
        String name = attributes.get("name");
        String tradingUnit = attributes.get("tradingUnit");
        Currency commodityCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new Commodity(name, tradingUnit, commodityCurrency, SemiRandomValuesGenerator.getRandomFloatNumber(6, 0.1f));
    }
    public Currency createCurrency(){
        //Creating new currency is strictly linked with creating new ExchengeRates table and also making links between them.
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCurrencyData();
        String name = attributes.get("name");
        HashSet<String> countriesWhereLegal = this.attributesGenerator.getRandomHashSetOfCountriesNames();

        Currency currency = new Currency(name, countriesWhereLegal, this.mainAsset, SemiRandomValuesGenerator.getRandomFloatNumber(6, 0.1f));

        return currency;
    }

    public MarketIndex createMarketIndex(ArrayList<Company> companiesByNow){
        HashMap<String,String> attributes = this.attributesGenerator.getRandomIndexData();
        String name = attributes.get("name");

        ArrayList<Company> companiesInIndex = new ArrayList<Company>();
        float cost = 0;
        for (Company company : companiesByNow) {
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5){
                companiesInIndex.add(company);
                //This should make sense that the more Assets we has in the Unit the more we earn.
                //Basically Index should work quite similarly
                cost += company.getShareValue();
            }
            
        }
        //In case no asset was chosen
        if (companiesInIndex.isEmpty()){
            int randomCompanyIndex = attributesGenerator.getRandomArrayIndex(companiesByNow);
            Company company = companiesByNow.get(randomCompanyIndex);
            companiesInIndex.add(company);
            cost += company.getShareValue();
        }
        MarketIndex index = new MarketIndex(name, "Market Index" ,companiesInIndex, this.mainAsset, 1/cost);
        index.getMainBankRates().removeLastRate(); // This is done artificially to start with some rate calculated after filtering
        index.updateRate();
        return index;

    }

    public DynamicMarketIndex createDynamicMarketIndex(ArrayList<Company> companiesByNow, String filterType){
        HashMap<String,String> attributes = this.attributesGenerator.getRandomIndexData();
        String name = attributes.get("name");
        int numberOfCompanies = SemiRandomValuesGenerator.getRandomIntNumber(16) + 5;
        CompaniesFilter filter = biggestFilter; //By Default it is biggest
        if (filterType.equals("startup")){
            filter = startupsFilter;
        }
        DynamicMarketIndex index = new DynamicMarketIndex(name, new ArrayList<Company>(companiesByNow), this.mainAsset, 0, filter, numberOfCompanies);
        index.getMainBankRates().removeLastRate(); // This is done artificially to start with some rate calculated after filtering
        index.updateIndex();

        return index;
    }
    public <T extends Asset> Market createMarket(ArrayList<Currency> currenciesByNow, ArrayList<T> assets, ArrayList<Asset> indices){
        HashMap<String,String> attributes = this.attributesGenerator.getRandomMarketData();
        String name = attributes.get("name");
        String country = attributes.get("country");
        String city = attributes.get("city");
        String address = attributes.get("address");
        float percentageOperationCost = SemiRandomValuesGenerator.getRandomFloatNumber(0.1f,0.1f);
        Currency tradingCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);
        
        HashMap<String, Asset> availableAssets = new HashMap<String, Asset>();
        //In general some Market may don't have all asssets from the very beginning. However now I assume all have all assets!.
        for (Asset asset : assets) {
            availableAssets.put(asset.getName(), asset);
        }

        if (indices != null){
            //In general some Market may don't have all asssets from the very beginning. However now I assume all have all assets!.
            for (Asset index : indices) {
            availableAssets.put(index.getName(), index);
        }
        }
        return new Market(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets, this.world);
    }


    public InvestmentFundUnit createFundUnit(InvestmentFund issuedBy, ArrayList<Asset> availableAssets) {
       
        
        HashMap<String, Float> boughtAssets = new HashMap<String, Float>();
        float cost = 0;
        for (Asset asset : availableAssets) {
            if (asset.getType() == "fund unit") continue;

            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.1){
                float amountBought = SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f);
                boughtAssets.put(asset.getName(), amountBought);
                //This should make sense that the more Assets we has in the Unit the more we earn.
                //Basically Index should work quite similarly
                cost += asset.calculateThisToDifferent(issuedBy.getRegisteredCurrency(), amountBought);
            }
            
        }
        //In case no asset was chosen
        if (boughtAssets.isEmpty()){
            Asset asset = this.attributesGenerator.getRandomAsset(availableAssets);
            boughtAssets.put(asset.getName(), SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f));
        }

        float fundPercentageProfit = SemiRandomValuesGenerator.getRandomFloatNumber(0.25f, 0.1f);

        return new InvestmentFundUnit(issuedBy.getName(), SemiRandomValuesGenerator.getRandomIntNumber(50), issuedBy, cost +  SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f), boughtAssets, fundPercentageProfit);

    }

    public void fillInitialBudgetRandomly(ArrayList<Currency> currenciesByNow, Trader trader){
        for (Currency currency : currenciesByNow) {
            float amount = SemiRandomValuesGenerator.getRandomFloatNumber(1000, 0.1f);
            trader.addBudget(currency, amount);
        }
    }
    
}
