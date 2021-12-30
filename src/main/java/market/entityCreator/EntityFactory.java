package market.entityCreator;

import market.traders.*;
import market.assets.Currency;
import market.assets.Share;
import market.assets.Asset;
import market.assets.Commodity;
import market.assets.InvestmentFundUnit;
import market.markets.*;
import java.util.*;
import market.exchangeRates.ExchangeRatesProvider;

public class EntityFactory {
    private SemiRandomValuesGenerator attributesGenerator = new SemiRandomValuesGenerator();
    
    public EntityFactory(){
       
    }

    public Company createCompany(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> company = this.attributesGenerator.getRandomCompanyData();
        String tradingIdentifier = company.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = company.get("name");
        String ipoDate = company.get("ipoDate");
        Float ipoShareValue = this.attributesGenerator.getRandomFloatNumber(5000) + 100;
        float openingPrice = this.attributesGenerator.getRandomFloatNumber(10000) + 100;
        float profit = this.attributesGenerator.getRandomFloatNumber(10000) + 100;
        float revenue = this.attributesGenerator.getRandomFloatNumber(5000) + 100;

        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new Company(tradingIdentifier, investmentBudget, name, ipoDate, ipoShareValue, openingPrice, profit, revenue, registeredCurrency);
    }

    public HumanInvestor createHumanInvestor(ArrayList<Currency> currenciesByNow) {
        HashMap<String, String> human = this.attributesGenerator.getRandomHumanInvestorData();
        String tradingIdentifier = human.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = human.get("name");
        String lastName = human.get("lastName");

        return new HumanInvestor(tradingIdentifier, investmentBudget, name, lastName);
    }

    public InvestmentFund createInvestmentFund(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> investmentFund = this.attributesGenerator.getRandomInvestmentFundData();
        String tradingIdentifier = investmentFund.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = investmentFund.get("name");
        String menagerFirstName = investmentFund.get("menagerFirstName");
        String menagerLastName = investmentFund.get("menagerLastName");
        
        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new InvestmentFund(tradingIdentifier, investmentBudget, name, menagerFirstName, menagerLastName, registeredCurrency);
    }

    public Commodity createCommodity(ArrayList<Currency> currenciesByNow){
        //Drowing some random attributes
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCommodityData();
        String name = attributes.get("name");
        String tradingUnit = attributes.get("tradingUnit");
        Currency commodityCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new Commodity(name, tradingUnit, commodityCurrency, attributesGenerator.getRandomFloatNumber(6));
    }
    public Currency createCurrency(ExchangeRatesProvider mainBankRates){
        //Creating new currency is strictly linked with creating new ExchengeRates table and also making links between them.
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCurrencyData();
        String name = attributes.get("name");
        HashSet<String> countriesWhereLegal = this.attributesGenerator.getRandomHashSetOfCountriesNames();

        mainBankRates.updateRate(name, attributesGenerator.getRandomFloatNumber(6));

        Currency currency = new Currency(name, countriesWhereLegal, mainBankRates);

        return currency;
    }

    public <T extends Asset> Market<T> createMarket(ArrayList<Currency> currenciesByNow, boolean withIndices, ArrayList<T> assets){
        HashMap<String,String> attributes = this.attributesGenerator.getRandomMarketData();
        String name = attributes.get("name");
        String country = attributes.get("country");
        String city = attributes.get("city");
        String address = attributes.get("address");
        float percentageOperationCost = this.attributesGenerator.getRandomFloatNumber((float)0.1);
        
        Currency tradingCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);
        
        HashMap<String, T> availableAssets = new HashMap<String, T>();
        //In general some Market may don't have all asssets from the very beginning. However now I assume all have all assets!.
        for (T asset : assets) {
            availableAssets.put(asset.getName(), asset);
        }

        if (withIndices){
            return new MarketWithIndices<T>(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets); 
        }
        return new Market<T>(name, country, city, address, percentageOperationCost, tradingCurrency, availableAssets);
    }


    public Share createShare(Company company){
        return new Share(company.getName(), attributesGenerator.getRandomIntNumber(50), company, company.getIpoShareValue());
    }

    public InvestmentFundUnit createFundUnit(InvestmentFund issuedBy, ArrayList<Asset> availableAssets) {
       
        
        HashMap<String, Float> boughtAssets = new HashMap<String, Float>();
        float cost = 0;
        for (Asset asset : availableAssets) {
            if (asset.getType() == "fund unit") continue;

            if (this.attributesGenerator.getRandomFloatNumber(1) < 0.1){
                float amountBought = this.attributesGenerator.getRandomFloatNumber(100);
                boughtAssets.put(asset.getName(), amountBought);
                //This should make sense that the more Assets we has in the Unit the more we earn.
                //Basically Index should work quite similarly
                cost += asset.calculateThisToDifferent(issuedBy.getRegisteredCurrency(), amountBought);
            }
            
        }
        //In case no asset was chosen
        if (boughtAssets.isEmpty()){
            Asset asset = this.attributesGenerator.getRandomAsset(availableAssets);
            boughtAssets.put(asset.getName(), this.attributesGenerator.getRandomFloatNumber(100));
        }

        float fundPercentageProfit = this.attributesGenerator.getRandomFloatNumber((float)0.25);

        return new InvestmentFundUnit(issuedBy.getName(), attributesGenerator.getRandomIntNumber(50), issuedBy, cost +  attributesGenerator.getRandomFloatNumber(100), boughtAssets, fundPercentageProfit);

    }
    
}
