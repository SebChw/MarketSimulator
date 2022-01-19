package market.entityCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import market.assets.Asset;
import market.assets.Currency;
import market.traders.InvestmentFund;
import market.world.World;

/**
 * Class for generating all kinds of random values. Can be used to generate
 * random values specific to our classes.
 * 
 * All functions get**Data returns a HashMap with keys as attributes names
 */
public class SemiRandomValuesGenerator implements Serializable {
    private String[] commodityNames = { "Cereal", "Silver", "Gas", "Oil", "Copper", "Les Paul", "Stratocaster", "SG",
            "Telecaster" };
    private String[] currencyNames = { "zloty", "euro", "dolar", "pound", "yang", "guitar pick", "czech koruna" };
    private String[] countriesNames = { "Poland", "US", "Great Britain", "China", "Australia", "Italy", "Spain",
            "Portugal" };
    private String[] tradingUnits = { "ounce", "gram", "liter", "barell", "tons" };
    private String[] companiesNames = { "EvilCorp", "Facebook", "Google", "Spotify", "Microsoft", "Gibson", "Fender",
            "Herley Benton", "PRS", "Gretsch", "Ibanez" };
    private String[] humanInvestorsSecondNames = { "Vivaldi", "Tchaikovsky", "Chopin", "Bach", "Mozart", "Satriani",
            "Vai", "Johnson", "Clapton", "Mayer", "King", "Charles", "Bonamassa", "Timmons" };
    private String[] humanInvestorsNames = { "Antonio", "Jan", "Sebastian", "Amadeus", "Fyderyk", "Andy", "Joe",
            "Steve", "Eric", "John", "BB.", "Ray", "Andy" };
    private String[] investmendFundsNames = { "Vanguard", "Fidelity", "iShares", "Invesco", "Goldman" };
    private String[] marketNames = { "Cartmax", "MegaPlex", "DollarSmart", "Marketaro", "Marketvio", "Guitar Center",
            "Thomman" };
    private String[] citiesNames = { "Singapur", "Krzywanice", "Posen", "New York", "Old York", "London" };
    private String[] streetsNames = { "Bourbon Street ", "Abbey Road ", "Lombard Street ", "Haji Lane ",
            "Passeig de Sant Joan " };
    private String[] marketIndicesNames = { "S&P", "Nasdaq Composite", "Dow Jones", "Average", "MSCI World", "FTSE" };
    private HashSet<String> tradingIdentifiers = new HashSet<String>();
    private int[] years = { 2000, 2005, 2010, 2020, 2015 };

    private static Random randomGenerator = new Random();
    private World world;

    public SemiRandomValuesGenerator(World world) {
        this.world = world;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomCompanyData() {
        HashMap<String, String> company = new HashMap<String, String>();
        getRandomValueAndPutToHashMap(this.companiesNames, "name", company);
        company.put("tradingIdentifier", this.getRandomIdentifier());
        company.put("ipoDate", this.getRandomDate());

        return company;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomHumanInvestorData() {
        HashMap<String, String> human = new HashMap<String, String>();
        getRandomValueAndPutToHashMap(this.humanInvestorsNames, "name", human);
        getRandomValueAndPutToHashMap(this.humanInvestorsSecondNames, "lastName", human);
        human.put("tradingIdentifier", this.getRandomIdentifier());

        return human;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomInvestmentFundData() {
        HashMap<String, String> investmentFund = new HashMap<String, String>();
        getRandomValueAndPutToHashMap(this.investmendFundsNames, "name", investmentFund);
        getRandomValueAndPutToHashMap(this.humanInvestorsNames, "menagerFirstName", investmentFund);
        getRandomValueAndPutToHashMap(this.humanInvestorsSecondNames, "menagerLastName", investmentFund);
        investmentFund.put("tradingIdentifier", this.getRandomIdentifier());

        return investmentFund;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomMarketData() {
        HashMap<String, String> market = new HashMap<String, String>();
        getRandomValueAndPutToHashMap(this.marketNames, "name", market);
        getRandomValueAndPutToHashMapNotUniqueNames(this.countriesNames, "country", market);
        getRandomValueAndPutToHashMapNotUniqueNames(this.citiesNames, "city", market);
        int name_idx = randomGenerator.nextInt(this.streetsNames.length);
        market.put("address", this.streetsNames[name_idx] + randomGenerator.nextInt(100));

        return market;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomIndexData() {
        HashMap<String, String> index = new HashMap<String, String>();
        getRandomValueAndPutToHashMap(this.marketIndicesNames, "name", index);

        return index;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomCommodityData() {
        HashMap<String, String> commodity = new HashMap<String, String>();

        getRandomValueAndPutToHashMap(this.commodityNames, "name", commodity);
        getRandomValueAndPutToHashMap(this.tradingUnits, "tradingUnit", commodity);

        return commodity;
    }

    /**
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRandomCurrencyData() {
        HashMap<String, String> currency = new HashMap<String, String>();

        getRandomValueAndPutToHashMap(this.currencyNames, "name", currency);

        return currency;
    }

    /**
     * Can be used to automatically generate countries where currency is available
     * 
     * @return HashSet<String>
     */
    public HashSet<String> getRandomHashSetOfCountriesNames() {
        HashSet<String> countriesNames = new HashSet<String>();

        int numberOfCountries = this.countriesNames.length;
        // Here we create Array with indices from which we will be drawing countries
        ArrayList<Integer> indicesOfCountries = new ArrayList<Integer>(numberOfCountries);
        for (int i = 0; i < numberOfCountries; i++) {
            indicesOfCountries.add(i);
        }

        int numberOfCountriesToBeDrawn = randomGenerator.nextInt(numberOfCountries) + 1; // Since this is exclusive by
                                                                                         // adding 1 we got range [1,
                                                                                         // size]

        // Here we consecutively randomly select index of one country add this country
        // to the HashSet and then
        // we remove it from possible indices so that is selection withouth reperition
        while (indicesOfCountries.size() > (numberOfCountries - numberOfCountriesToBeDrawn)) {
            int index = randomGenerator.nextInt(indicesOfCountries.size());

            countriesNames.add(this.countriesNames[indicesOfCountries.remove(index)]);
        }

        return countriesNames;

    }

    /**
     * Function that returns random number from [0,1] times multiplier
     * 
     * @param multiplier
     * @return float
     */
    public static float getRandomFloatNumber(float multiplier) {
        return (float) ((randomGenerator.nextFloat()) * multiplier); // nextFloat returns a floating point number
                                                                     // between [0.0, 1.0]
    }

    /**
     * Function that returns random number from [0+offset,1+offset] times
     * multiplier. To somehow prevent very small values
     * 
     * @param multiplier
     * @param offset
     * @return float
     */
    public static float getRandomFloatNumber(float multiplier, float offset) {
        return (float) ((randomGenerator.nextFloat() + offset) * multiplier); // nextFloat returns a floating point
                                                                              // number between [0.0, 1.0]
    }

    /**
     * Function returning random integer from (0, max]
     * 
     * @param max
     * @return int
     */
    public static int getRandomIntNumber(int max) {
        return randomGenerator.nextInt(max); // nextFloat returns a floating point number between [0.0, 1.0]
    }

    /**
     * Function returning random value from normal distribution with given mean and
     * std
     * 
     * @param mean
     * @param std
     * @return float
     */
    public static float getFromNormal(float mean, float std) {
        return (float) (randomGenerator.nextGaussian() * std + mean);
    }

    /**
     * This function randomly select String from an array -> put it to the hashMap
     * under given key. Additionally tu assure
     * uniqueness of values it ads a _ character at the end of every added string.
     * 
     * @param array
     * @param key
     * @param randomAttributes
     */
    public void getRandomValueAndPutToHashMap(String[] array, String key, HashMap<String, String> randomAttributes) {
        int name_idx = randomGenerator.nextInt(array.length);
        randomAttributes.put(key, array[name_idx]);
        array[name_idx] = array[name_idx] + "_";
    }

    /**
     * * This function randomly select String from an array -> put it to the hashMap
     * under given key.
     * 
     * @param array
     * @param key
     * @param randomAttributes
     */
    public void getRandomValueAndPutToHashMapNotUniqueNames(String[] array, String key,
            HashMap<String, String> randomAttributes) {
        int name_idx = randomGenerator.nextInt(array.length);
        randomAttributes.put(key, array[name_idx]);
    }

    /**
     * Given array returns randomly some available index
     * 
     * @param array
     * @return int
     */
    public static int getRandomArrayIndex(ArrayList<?> array) {
        return randomGenerator.nextInt(array.size());
    }

    /**
     * @param array
     * @return int
     */
    public static int getRandomArrayIndex(List<?> array) {
        return randomGenerator.nextInt(array.size());
    }

    /**
     * Function that randomly generates traderID using RandomString class
     * 
     * @return String
     */
    public String getRandomIdentifier() {
        String id = RandomString.getAlphaNumericString(10);
        while (!this.tradingIdentifiers.add(id)) {
            id = RandomString.getAlphaNumericString(10);
        }
        return id;
    }

    public static String getRandomIdentifier(int length) {
        return RandomString.getAlphaNumericString(length);
    }

    /**
     * Function generating random date in a format yyyy-mm-dd. This format is
     * appropriate for LocalDate object
     * 
     * @return String
     */
    public String getRandomDate() {
        int year = this.years[randomGenerator.nextInt(this.years.length)];
        int month = randomGenerator.nextInt(12) + 1;
        int day = randomGenerator.nextInt(28) + 1;
        // The locale is in case someone uses Arabic language
        return Integer.toString(year) + "-" + String.format((Locale) null, "%02d", month) + "-"
                + String.format((Locale) null, "%02d", day);
    }

    /**
     * @param currenciesByNow
     * @return Currency
     */
    public Currency getRandomCurrency(ArrayList<Currency> currenciesByNow) {
        int randomCurrencyIndex = getRandomArrayIndex(currenciesByNow);
        return currenciesByNow.get(randomCurrencyIndex);
    }

    /**
     * @param assetsByNow
     * @return Asset
     */
    public Asset getRandomAsset(ArrayList<? extends Asset> assetsByNow) {
        int randomAssetIndex = getRandomArrayIndex(assetsByNow);
        return assetsByNow.get(randomAssetIndex);
    }

    /**
     * @param funds
     * @return InvestmentFund
     */
    public static InvestmentFund getRandomInvestmentFund(ArrayList<InvestmentFund> funds) {
        int randomIndex = getRandomArrayIndex(funds);
        return funds.get(randomIndex);

    }

    /**
     * Function returning either true or false depending on the bullProbability set
     * in the program
     * 
     * @return boolean
     */
    public boolean getBearIndicator() {
        float bullProbability = world.getBullProbability();
        boolean bearIndicator = true;
        if (getRandomFloatNumber(1) < bullProbability) {
            bearIndicator = false;
        }

        return bearIndicator;
    }

}
