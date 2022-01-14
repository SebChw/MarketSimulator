package market.world;

import market.traders.*;
import market.assets.*;
import market.assets.marketIndex.*;
import market.markets.*;

import java.util.ArrayList;
import java.util.Random;

import market.entityCreator.*;
import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;

/**
 * Class bein able to add new objects to the world in that case it does it
 * randomly.
 * Can write different adder that will be able to do it e.g with only user
 * specified values.
 */
public class ObjectsAdder {

    private WorldContainer worldContainer;
    private TraderFactory TraderFactory;
    private AssetFactory AssetFactory;
    private MarketFactory MarketFactory;

    private Random randomGenerator = new Random();

    private ObjectCounter objectCounter;

    ObjectsAdder(WorldContainer container, World world, ObjectCounter objectCounter) {
        this.worldContainer = container;
        this.AssetFactory = new AssetFactory(world.getMainAsset(), world);
        this.TraderFactory = new TraderFactory(world);
        this.MarketFactory = new MarketFactory(world);
        this.objectCounter = objectCounter;

    }

    /**
     * Add new Randomly generated currency to the world
     * 
     * @return Currency
     */
    public Currency addNewCurrency() {
        Currency cur = this.AssetFactory.createCurrency();
        worldContainer.addNewCurrency(cur);
        addAssetToMarkets(cur);
        objectCounter.changeNumberOfCurrencies(1);

        return cur;
    }

    /**
     * Add randomly created Commodity to the world
     * 
     * @return Commodity
     */
    public Commodity addNewCommodity() {
        Commodity com = this.AssetFactory.createCommodity(worldContainer.getCurrencies());
        worldContainer.addNewCommodity(com);
        objectCounter.changeNumberOfCommodities(1);
        addAssetToMarkets(com);

        return com;
    }

    /**
     * Add randomly generated company and in the same time Share of that company to
     * the simulation. It also automatically starts the Company as a thread.
     * 
     * @return Company
     */
    public Company addNewCompany() {
        Company company = null;
        // In that moment we should create Share object too!
        // We can create that object only having some currencies in the world
        if (objectCounter.getNumberOfCurrencies() > 0) {
            company = this.TraderFactory.createCompany(worldContainer.getCurrencies(),
                    worldContainer.getDynamicMarketIndices());

            worldContainer.addNewCompany(company);
            objectCounter.changeNumberOfCompanies(1);

            Share share = company.getShare();
            worldContainer.addNewShare(share);
            addAssetToMarkets(share);

            // As we add company we must update all dynamic indices
            for (DynamicMarketIndex index : worldContainer.getDynamicMarketIndices()) {
                index.update(company);
                index.updateIndex();
            }
        } else {
            System.out.println("Can't create Company withouth any Currencies!");
        }

        TraderFactory.fillInitialBudgetRandomly(worldContainer.getCurrencies(), company);
        (new Thread(company)).start();
        return company;
    }

    /**
     * Add randomly generated human investor and starts it as a thread
     * 
     * @return HumanInvestor
     */
    public HumanInvestor addNewHumanInvestor() {
        HumanInvestor human = this.TraderFactory.createHumanInvestor(worldContainer.getCurrencies());
        worldContainer.addNewHumanInvestor(human);
        objectCounter.changeNumberOfHumanInvestors(1);
        TraderFactory.fillInitialBudgetRandomly(worldContainer.getCurrencies(), human);
        (new Thread(human)).start();
        return human;
    }

    /**
     * Add randomly generated investment fund to the simulation and runs its thread
     * 
     * @param controller controller would be needed if we have many worlds to
     *                   properly generate investment fund units
     * @return InvestmentFund
     */
    public InvestmentFund addNewInvestmentFund(MainPanelController controller) {
        InvestmentFund fund = null;
        if (objectCounter.getNumberOfCurrencies() > 0) {
            fund = this.TraderFactory.createInvestmentFund(worldContainer.getCurrencies(), controller);
            worldContainer.addNewInvestmentFund(fund);
            objectCounter.changeNumberOfInvestmentFunds(1);
        } else {
            System.out.println("Can't create Investment Fund withouth any Currencies!");
        }
        TraderFactory.fillInitialBudgetRandomly(worldContainer.getCurrencies(), fund);
        (new Thread(fund)).start();
        return fund;
    }

    /**
     * Randomly Add new Commodity/Asset/Stock market to the world
     * 
     * @return Market
     */
    public Market addNewRandomMarket() {
        Market market = null;
        if (objectCounter.getNumberOfCurrencies() > 0) {
            float uniformNumber = randomGenerator.nextFloat();

            if (uniformNumber < 0.33 && !worldContainer.getShares().isEmpty()) {
                ArrayList<Asset> allIndices = new ArrayList<Asset>();
                allIndices.addAll(worldContainer.getMarketIndices());
                allIndices.addAll(worldContainer.getDynamicMarketIndices());
                market = this.MarketFactory.createMarket(worldContainer.getCurrencies(), worldContainer.getShares(),
                        allIndices);
            } else if (uniformNumber < 0.665 && !worldContainer.getCommodities().isEmpty()) {
                market = this.MarketFactory.createMarket(worldContainer.getCurrencies(),
                        worldContainer.getCommodities(), null);
            } else {
                market = this.MarketFactory.createMarket(worldContainer.getCurrencies(), worldContainer.getCurrencies(),
                        null);
            }

            worldContainer.addNewMarket(market);
            objectCounter.changeNumberOfMarkets(1);

        } else {
            System.out.println("You can't create Market Withouth any currency!");
        }
        return market;
    }

    /**
     * Automatically add new Market Index to the world
     * 
     * @return MarketIndex
     */
    public MarketIndex addNewMarketIndex() {
        MarketIndex marketIndex = null;
        if (worldContainer.getCompanies().isEmpty()) {
            System.out.println("You can'y create market Index with no companies in the world!");
        }
        marketIndex = this.AssetFactory.createMarketIndex(worldContainer.getCompanies());
        worldContainer.addNewMarketIndex(marketIndex);
        objectCounter.changeNumberOfIndices(1);
        addAssetToMarkets(marketIndex);
        return marketIndex;
    }

    /**
     * Add new dynamic market index to the world with rule of sorting companies
     * selected randomly
     * 
     * @return DynamicMarketIndex
     */
    public DynamicMarketIndex addNewDynamicMarketIndex() {
        DynamicMarketIndex marketIndex = null;
        if (worldContainer.getCompanies().isEmpty()) {
            System.out.println("You can't create market Index with no companies in the world!");
        }

        String type = "biggest";
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5)
            type = "startup";

        marketIndex = this.AssetFactory.createDynamicMarketIndex(worldContainer.getCompanies(), type);
        worldContainer.addNewDynamicMarketIndex(marketIndex);
        objectCounter.changeNumberOfDynamicIndices(1);
        addAssetToMarkets(marketIndex);
        return marketIndex;

    }

    /**
     * Add randomly generated Investment Fund Unit
     * 
     * @param issuedBy Which investment fund has issued this asset
     * @return InvestmentFundUnit
     */
    public InvestmentFundUnit addNewInvestmentFundUnit(InvestmentFund issuedBy) {
        // This method won't be executed very often so thaat I can create new ArrayList
        // each time
        if (objectCounter.getAmountOfAssets() > 0) {
            InvestmentFundUnit unit = this.AssetFactory.createFundUnit(issuedBy,
                    worldContainer.getAllAssets());
            worldContainer.addNewInvestmentFundUnit(unit);
            objectCounter.changeNumberOfInvestmentFundUnits(1);
            issuedBy.getController().addInvestmentFundUnit(unit);
            return unit;
        } else {
            System.out.println("You can't create FundUnit withouth any Assets on the Market");
            return null;
        }

    }

    /**
     * Takes all markets and add given asset to them with some probability
     * 
     * @param asset
     */
    public void addAssetToMarkets(Asset asset) {
        for (Market market : worldContainer.getAllMarkets()) {
            if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.8) {
                market.addNewAsset(asset);
            }
        }
    }
}
