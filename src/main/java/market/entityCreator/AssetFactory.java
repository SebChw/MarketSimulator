package market.entityCreator;

import market.traders.*;
import market.world.World;
import market.assets.Currency;
import market.assets.Asset;
import market.assets.Commodity;
import market.assets.InvestmentFundUnit;
import java.util.*;
import market.assets.marketIndex.MarketIndex;
import market.assets.marketIndex.*;

/**
 * Function for creating new Assets
 */
public class AssetFactory {
    private SemiRandomValuesGenerator attributesGenerator;
    private String mainAsset;

    // We can select it randomly
    private CompaniesFilter biggestFilter = new BiggestCompaniesFilter();
    private CompaniesFilter startupsFilter = new StartupsCompaniesFilter();
    private World world;

    public AssetFactory(String mainAsset, World world) {
        this.mainAsset = mainAsset;
        this.world = world;
        this.attributesGenerator = new SemiRandomValuesGenerator(world);
    }

    /**
     * @param currenciesByNow Commodities are sold in some currency so we need to
     *                        select one
     * @return Commodity
     */
    public Commodity createCommodity(ArrayList<Currency> currenciesByNow) {
        // Drowing some random attributes
        HashMap<String, String> attributes = this.attributesGenerator.getRandomCommodityData();
        String name = attributes.get("name");
        String tradingUnit = attributes.get("tradingUnit");
        Currency commodityCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        return new Commodity(name, tradingUnit, commodityCurrency,
                SemiRandomValuesGenerator.getRandomFloatNumber(6, 0.1f));
    }

    /**
     * @return Currency
     */
    public Currency createCurrency() {
        // Creating new currency is strictly linked with creating new ExchengeRates
        // table and also making links between them.
        HashMap<String, String> attributes = this.attributesGenerator.getRandomCurrencyData();
        String name = attributes.get("name");
        HashSet<String> countriesWhereLegal = this.attributesGenerator.getRandomHashSetOfCountriesNames();

        Currency currency = new Currency(name, countriesWhereLegal, this.mainAsset,
                SemiRandomValuesGenerator.getRandomFloatNumber(6, 0.1f));

        return currency;
    }

    /**
     * @param companiesByNow companies that will be randomly selected to the index
     * @return MarketIndex
     */
    public MarketIndex createMarketIndex(ArrayList<Company> companiesByNow) {
        HashMap<String, String> attributes = this.attributesGenerator.getRandomIndexData();
        String name = attributes.get("name");

        ArrayList<Company> companiesInIndex = new ArrayList<Company>();
        for (Company company : companiesByNow) {
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5) {
                companiesInIndex.add(company);

            }

        }
        // In case no asset was chosen
        if (companiesInIndex.isEmpty()) {
            int randomCompanyIndex = SemiRandomValuesGenerator.getRandomArrayIndex(companiesByNow);
            Company company = companiesByNow.get(randomCompanyIndex);
            companiesInIndex.add(company);
        }
        MarketIndex index = new MarketIndex(name, companiesInIndex, this.mainAsset);
        return index;

    }

    /**
     * @param companiesByNow In that case all companies will be just filtered
     * @param filterType     currently can be "startup" or "biggest"
     * @return DynamicMarketIndex
     */
    public DynamicMarketIndex createDynamicMarketIndex(ArrayList<Company> companiesByNow, String filterType) {
        HashMap<String, String> attributes = this.attributesGenerator.getRandomIndexData();
        String name = attributes.get("name");
        int numberOfCompanies = SemiRandomValuesGenerator.getRandomIntNumber(5) + 2;
        CompaniesFilter filter = biggestFilter; // By Default it is biggest
        if (filterType.equals("startup")) {
            filter = startupsFilter;
        }

        DynamicMarketIndex index = new DynamicMarketIndex(name, new ArrayList<Company>(companiesByNow), this.mainAsset,
                filter, numberOfCompanies);

        for (Company company : companiesByNow) {
            company.registerObserver(index);
        }

        return index;
    }

    /**
     * @param issuedBy        Issue fund that releases that unit
     * @param availableAssets Some random assets will be added to that unit
     * @return InvestmentFundUnit
     */
    public InvestmentFundUnit createFundUnit(InvestmentFund issuedBy, ArrayList<Asset> availableAssets) {
        HashMap<String, Float> boughtAssets = new HashMap<String, Float>();
        for (Asset asset : availableAssets) {
            if (asset.getType().equals("Investment Fund Unit") || asset.getType().equals("Market Index")
                    || asset.getType().equals("Share"))
                continue;

            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.1) {
                float amountBought = SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f);
                boughtAssets.put(asset.getName(), amountBought);
            }

        }
        // In case no asset was chosen
        if (boughtAssets.isEmpty()) {
            Asset asset = this.attributesGenerator.getRandomAsset(availableAssets);
            float amountBought = SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f);
            boughtAssets.put(asset.getName(), amountBought);
        }

        return new InvestmentFundUnit(issuedBy.getName() + issuedBy.getIssuedFundsAmount(), issuedBy, boughtAssets,
                world);

    }

}
