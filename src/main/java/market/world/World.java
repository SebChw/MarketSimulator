package market.world;

import market.traders.*;
import market.assets.*;
import market.assets.marketIndex.*;

import java.time.LocalDate;

import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;

/**
 * World is basically a container of all markets, assets, traders and is also
 * has an interface for adding new objects to it
 * 
 */
public class World {
    private ObjectCounter objectCounter = new ObjectCounter();
    private WorldContainer worldContainer = new WorldContainer();
    private ObjectsAdder objectsAdder;

    private volatile LocalDate currentTime = LocalDate.now(); // Update at constant intervals of time

    private float transactionProbability = (float) 0.5;
    private float bullProbability = (float) 0.5;

    // everything is backed in some Main Asset!
    private String mainAsset;

    public World(String mainAsset) {
        this.mainAsset = mainAsset;
        this.objectsAdder = new ObjectsAdder(worldContainer, this, objectCounter);
    }

    /**
     * Checks ratio of assets to investors and if needed automaticaly creates new
     * asset/investor
     * 
     * @param controller controller on which simulation is run
     */
    public void checkNeedOfCreatingInvestor(MainPanelController controller) {
        // numberOfCompanies == numberOfShares
        float numberOfAssets = objectCounter.getAmountOfAssets();
        float numberOfInvestors = objectCounter.getAmountOfInvestors();
        if (numberOfInvestors / numberOfAssets < 0.5) {
            float randomNumber = SemiRandomValuesGenerator.getRandomFloatNumber(1);
            if (randomNumber < 0.5) {
                controller.addHumanInvestor();
            } else {
                controller.addInvestmentFund();
            }
        }
    }

    /**
     * @return ObjectCounter
     */
    public ObjectCounter getObjectCounter() {
        return this.objectCounter;
    }

    public synchronized void addOneDay() {
        currentTime = currentTime.plusDays(1);
    }

    /**
     * @return LocalDate
     */
    public LocalDate getCurrentTime() {
        return currentTime;
    }

    /**
     * @return float
     */
    public float getTransactionProbability() {
        return transactionProbability;
    }

    /**
     * @param probability
     */
    public void setTransactionProbability(float probability) {
        transactionProbability = probability;
        System.out.println(transactionProbability);
    }

    /**
     * @return float
     */
    public float getBullProbability() {
        return bullProbability;
    }

    /**
     * @param probability
     */
    public void setBullProbability(float probability) {
        bullProbability = probability;
        System.out.println(bullProbability);
    }

    public void updateAllRates() {
        for (Asset asset : worldContainer.getAllAssets()) {
            asset.updateRate();
            // asset.changeAmountOfOwners(1);
        }
    }

    public void updateAllIndices() {
        for (Company company : worldContainer.getCompanies()) {
            company.notifyObservers();
        }
        for (DynamicMarketIndex index : worldContainer.getDynamicMarketIndices()) {
            index.updateIndex();
            // asset.changeAmountOfOwners(1);
        }
    }

    /**
     * @return String
     */
    public String getMainAsset() {
        return mainAsset;
    }

    public WorldContainer getWorldContainer() {
        return worldContainer;
    }

    public ObjectsAdder getObjectsAdder() {
        return objectsAdder;
    }

    /**
     * @param name name of asset wanted to obtain
     * @return Asset
     */
    public Asset getParticularAsset(String name) {
        // This method Will be executed veeeery often that's Why I should provide O(1)
        // lookup
        return worldContainer.getAllAssetsHashMap().get(name);
    }
}
