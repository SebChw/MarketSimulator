package market.entityCreator;

import market.traders.*;
import market.assets.Currency;
import market.assets.Asset;
import market.assets.Commodity;
import market.markets.*;
import java.util.*;
import market.exchangeRates.ExchangeRatesProvider;

public class EntityFactory {
    SemiRandomValuesGenerator attributesGenerator = new SemiRandomValuesGenerator();
    public EntityFactory(){
       
    }

    public Company createCompany(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> company = this.attributesGenerator.getRandomCompanyData();
        String tradingIdentifier = company.get("tradingIdentifier");
        HashMap<Currency, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = company.get("name");
        String ipoDate = company.get("ipoDate");
        float ipoShareValue = this.attributesGenerator.getRandomFloatNumber(5000);
        float openingPrice = this.attributesGenerator.getRandomFloatNumber(10000);
        float profit = this.attributesGenerator.getRandomFloatNumber(10000);
        float revenue = this.attributesGenerator.getRandomFloatNumber(5000);

        return new Company(tradingIdentifier, investmentBudget, name, ipoDate, ipoShareValue, openingPrice, profit, revenue);
    }

    public HumanInvestor createHumanInvestor(ArrayList<Currency> currenciesByNow) {
        HashMap<String, String> human = this.attributesGenerator.getRandomHumanInvestorData();
        String tradingIdentifier = human.get("tradingIdentifier");
        HashMap<Currency, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = human.get("name");
        String lastName = human.get("lastName");

        return new HumanInvestor(tradingIdentifier, investmentBudget, name, lastName);
    }

    public InvestmentFund createInvestmentFund(ArrayList<Currency> currenciesByNow){
        HashMap<String, String> investmentFund = this.attributesGenerator.getRandomInvestmentFundData();
        String tradingIdentifier = investmentFund.get("tradingIdentifier");
        HashMap<Currency, Float> investmentBudget = this.attributesGenerator.getRandomInitialBudget(currenciesByNow);
        String name = investmentFund.get("name");
        String menagerFirstName = investmentFund.get("menagerFirstName");
        String menagerLastName = investmentFund.get("menagerLastName");
        
        return new InvestmentFund(tradingIdentifier, investmentBudget, name, menagerFirstName, menagerLastName);
    }

    public Commodity createCommodity(ArrayList<Currency> currenciesByNow){
        //Drowing some random attributes
        int randomCurrencyIndex = this.attributesGenerator.getRandomArrayIndex(currenciesByNow);
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCommodityData();
        String name = attributes.get("name");
        String tradingUnit = attributes.get("tradingUnit");
        Currency commodityCurrency = currenciesByNow.get(randomCurrencyIndex);

        //Setting some random ratio
        ExchangeRatesProvider exchangeRates = new ExchangeRatesProvider(attributes.get("name"), "commodity");
        float commodityToCurrency = attributesGenerator.getRandomFloatNumber(6);
        exchangeRates.updateRate(commodityCurrency.getName(), commodityToCurrency);
        commodityCurrency.updateRate(name, 1/commodityToCurrency);

        return new Commodity(name, tradingUnit, commodityCurrency, exchangeRates);
    }
    public Currency createCurrency(ArrayList<ExchangeRatesProvider> exchengeRatesByNow){
        //Creating new currency is strictly linked with creating new ExchengeRates table and also making links between them.
        HashMap<String,String> attributes = this.attributesGenerator.getRandomCurrencyData();
        HashSet<String> countriesWhereLegal = this.attributesGenerator.getRandomHashSetOfCountriesNames();

        ExchangeRatesProvider exchangeRates = new ExchangeRatesProvider(attributes.get("name"), "currency");
        exchangeRates.updateRate("gold", attributesGenerator.getRandomFloatNumber(6));
        //When we create new currency we must update links to all other currencies. We could possible store only value w.r.t to gold and recalculate things
        //However we need to store values of currencies w.r.t time.

        for (ExchangeRatesProvider rate : exchengeRatesByNow) {
            float our_to_considered = exchangeRates.getRate("gold") / rate.getRate("gold"); //We divide our rate w.r to gold by some other currency value
            exchangeRates.updateRate(rate.getNameOfAsset(), our_to_considered);
            rate.updateRate(exchangeRates.getNameOfAsset(), 1/our_to_considered);
        }

        exchengeRatesByNow.add(exchangeRates);

        Currency currency = new Currency(attributes.get("name"), countriesWhereLegal, exchangeRates);

        return currency;
    }

    public <T extends Asset> Market<T> createMarket(ArrayList<Currency> currenciesByNow, boolean withIndices){
        HashMap<String,String> attributes = this.attributesGenerator.getRandomMarketData();
        String name = attributes.get("name");
        String country = attributes.get("country");
        String city = attributes.get("city");
        String address = attributes.get("address");
        float percentageOperationCost = this.attributesGenerator.getRandomFloatNumber((float)0.1);
        
        int randomCurrencyIndex = this.attributesGenerator.getRandomArrayIndex(currenciesByNow);
        Currency tradingCurrency = currenciesByNow.get(randomCurrencyIndex);
        
        if (withIndices){
            return new MarketWithIndices<T>(name, country, city, address, percentageOperationCost, tradingCurrency); 
        }
        return new Market<T>(name, country, city, address, percentageOperationCost, tradingCurrency);
    }

    
}
