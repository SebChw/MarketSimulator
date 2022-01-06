package market.entityCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.LinkedList;
import market.App;
import market.assets.Asset;
import market.assets.Currency;
import market.traders.Trader;
import market.world.World;

public class SemiRandomValuesGenerator {
    private String[] commodityNames = {"Cereal","Silver","Gas", "Oil", "Copper"};
    private String[] currencyNames = {"zloty","euro","dolar", "pound", "yang"};
    private String[] countriesNames= {"Poland","US","Great Britain", "China", "Australia", "Italy", "Spain", "Portugal"};
    private String[] tradingUnits = {"ounce", "gram", "liter", "barell", "tons"};
    private String[] companiesNames = {"EvilCorp", "Facebook", "Google", "Spotify", "Microsoft"};
    private String[] humanInvestorsSecondNames = {"Vivaldi", "Tchaikovsky", "Chopin", "Bach", "Mozart"};
    private String[] humanInvestorsNames = {"Antonio", "Jan", "Sebastian", "Amadeus", "Fyderyk"};
    private String[] investmendFundsNames = {"Vanguard", "Fidelity", "iShares", "Invesco", "Goldman"};
    private String[] marketNames = {"Cartmax", "MegaPlex", "DollarSmart", "Marketaro", "Marketvio"};
    private String[] citiesNames = {"Singapur", "Krzywanice", "Posen", "New York", "Old York"};
    private String[] streetsNames = {"Bourbon Street ", "Abbey Road ", "Lombard Street ", "Haji Lane ", "Passeig de Sant Joan "};
    private String[] marketIndicesNames = {"S&P", "Nasdaq Composite", "Dow Jones", "Average", "MSCI World", "FTSE"};
    private HashSet<String> tradingIdentifiers = new HashSet<String>();
    private int[] years = {2000,2005,2010,2020,2015};

    private static Random randomGenerator = new Random();
    private World world;

    public SemiRandomValuesGenerator(World world){
        this.world = world;       
    }

    public HashMap<String,String> getRandomCompanyData(){
        HashMap<String,String> company = new HashMap<String,String>();
        getRandomValueAndPutToHashMap(this.companiesNames, "name", company);
        company.put("tradingIdentifier", this.getRandomIdentifier());
        company.put("ipoDate", this.getRandomDate());

        return company;
    }
    public HashMap<String,String> getRandomHumanInvestorData(){
        HashMap<String,String> human = new HashMap<String,String>();
        getRandomValueAndPutToHashMap(this.humanInvestorsNames, "name", human);
        getRandomValueAndPutToHashMap(this.humanInvestorsSecondNames, "lastName", human);
        human.put("tradingIdentifier", this.getRandomIdentifier());

        return human;
    }
    public HashMap<String,String> getRandomInvestmentFundData(){
        HashMap<String,String> investmentFund = new HashMap<String,String>();
        getRandomValueAndPutToHashMap(this.investmendFundsNames, "name", investmentFund);
        getRandomValueAndPutToHashMap(this.humanInvestorsNames, "menagerFirstName", investmentFund);
        getRandomValueAndPutToHashMap(this.humanInvestorsSecondNames, "menagerLastName", investmentFund);
        investmentFund.put("tradingIdentifier", this.getRandomIdentifier());

        return investmentFund;
    }
    public HashMap<String,String> getRandomMarketData(){
        HashMap<String,String> market = new HashMap<String,String>();
        getRandomValueAndPutToHashMap(this.marketNames, "name", market);
        getRandomValueAndPutToHashMapNotUniqueNames(this.countriesNames, "country", market);
        getRandomValueAndPutToHashMapNotUniqueNames(this.citiesNames, "city", market);
        int name_idx = randomGenerator.nextInt(this.streetsNames.length);
        market.put("address", this.streetsNames[name_idx] + randomGenerator.nextInt(100));


        return market;
    }
    public HashMap<String,String> getRandomIndexData(){
        HashMap<String, String> index = new HashMap<String,String>();
        getRandomValueAndPutToHashMap(this.marketIndicesNames, "name", index);

        return index;
    }
    public HashMap<String,String> getRandomCommodityData(){
        HashMap<String,String> commodity = new HashMap<String,String>();

        getRandomValueAndPutToHashMap(this.commodityNames, "name", commodity);
        getRandomValueAndPutToHashMap(this.tradingUnits, "tradingUnit", commodity);

        return commodity;
    }

    public HashMap<String,String> getRandomCurrencyData(){
        HashMap<String,String> currency = new HashMap<String,String>();

        getRandomValueAndPutToHashMap(this.currencyNames, "name", currency);

        return currency;
    }

    public HashSet<String> getRandomHashSetOfCountriesNames(){
        HashSet<String> countriesNames = new HashSet<String>();

        int numberOfCountries = this.countriesNames.length;
        //Here we create Array with indices from which we will be drawing countries
        ArrayList<Integer> indicesOfCountries = new ArrayList<Integer>(numberOfCountries);
        for (int i = 0; i < numberOfCountries; i++) {
            indicesOfCountries.add(i);
        }
        
        int numberOfCountriesToBeDrawn = randomGenerator.nextInt(numberOfCountries) + 1; // Since this is exclusive by adding 1 we got range [1, size]

        //Here we consecutively randomly select index of one country add this country to the HashSet and then
        //we remove it from possible indices so that is selection withouth reperition
        while (indicesOfCountries.size() > (numberOfCountries - numberOfCountriesToBeDrawn)){
            int index = randomGenerator.nextInt(indicesOfCountries.size());

            countriesNames.add(this.countriesNames[indicesOfCountries.remove(index)]);
        }

        return countriesNames;

    }
    public static float getRandomFloatNumber(float multiplier){
        return (float) ((randomGenerator.nextFloat()) * multiplier); // nextFloat returns a floating point number between [0.0, 1.0]
    }

    public static float getRandomFloatNumber(float multiplier, float offset){
        return (float) ((randomGenerator.nextFloat() + offset) * multiplier); // nextFloat returns a floating point number between [0.0, 1.0]
    }
 
    public static int getRandomIntNumber(int max){
        return randomGenerator.nextInt(max); // nextFloat returns a floating point number between [0.0, 1.0]
    }
    public static float getFromNormal(float mean, float std){
        return (float) (randomGenerator.nextGaussian() * std + mean);
    }

    public void getRandomValueAndPutToHashMap(String[] array, String key,  HashMap<String,String> randomAttributes){
        int name_idx = randomGenerator.nextInt(array.length);
        randomAttributes.put(key, array[name_idx]);
        array[name_idx] = array[name_idx] + "_";
    }

    public void getRandomValueAndPutToHashMapNotUniqueNames(String[] array, String key,  HashMap<String,String> randomAttributes){
        int name_idx = randomGenerator.nextInt(array.length);
        randomAttributes.put(key, array[name_idx]);
    }

    public static int getRandomArrayIndex(ArrayList<?> array){
        return randomGenerator.nextInt(array.size());
    }

    public static int getRandomArrayIndex(List<?> array){
        return randomGenerator.nextInt(array.size());
    }

    public String getRandomIdentifier(){
        String id = RandomString.getAlphaNumericString(10);
        while (! this.tradingIdentifiers.add(id)){
            id = RandomString.getAlphaNumericString(10);
        }
        return id;
    }

    public String getRandomDate(){
        int year = this.years[randomGenerator.nextInt(this.years.length)];
        int month = randomGenerator.nextInt(12) + 1;
        int day = randomGenerator.nextInt(28) + 1;
        //The locale is in case someone uses Arabic language
        return Integer.toString(year) + "-" + String.format((Locale) null,"%02d", month) + "-" + String.format((Locale) null, "%02d", day);
    }

    public Currency getRandomCurrency(ArrayList<Currency> currenciesByNow){
        int randomCurrencyIndex = this.getRandomArrayIndex(currenciesByNow);
        return currenciesByNow.get(randomCurrencyIndex);
    }

    public Asset getRandomAsset(ArrayList<? extends Asset> assetsByNow){
        int randomAssetIndex = this.getRandomArrayIndex(assetsByNow);
        return assetsByNow.get(randomAssetIndex);
    }

    public boolean getBearIndicator(){
        float bullProbability = world.getBullProbability();
        boolean bearIndicator = true;
        if (getRandomFloatNumber(1) < bullProbability){
            bearIndicator = false;
        }

        return bearIndicator;
    }
    
}
