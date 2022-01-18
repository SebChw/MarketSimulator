package market.world;

import market.traders.*;
import market.AppInitializer;
import market.assets.*;
import market.assets.marketIndex.*;

import java.io.*;
import java.time.LocalDate;

import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;

/**
 * World is basically a container of all markets, assets, traders and is also
 * has an interface for adding new objects to it
 * 
 */
public class World implements Serializable {
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
    }

    public void updateAllRates() {
        for (Company company : worldContainer.getCompanies()) {
            company.notifyObservers();
        }
        for (Asset asset : worldContainer.getAllAssets()) {
            asset.updateRate();
            // asset.changeAmountOfOwners(1);
        }
    }

    // public void updateAllIndices() {

    // for (DynamicMarketIndex index : worldContainer.getDynamicMarketIndices()) {
    // index.updateIndex();
    // // asset.changeAmountOfOwners(1);
    // }
    // }

    /**
     * @return String
     */
    public String getMainAsset() {
        return mainAsset;
    }

    /**
     * @return WorldContainer
     */
    public WorldContainer getWorldContainer() {
        return worldContainer;
    }

    /**
     * @return ObjectsAdder
     */
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

    /**
     * First stops all threads and then restarts state of the simulation to empty.
     */
    public void restart() {
        removeThreads();

        objectCounter = new ObjectCounter();
        worldContainer = new WorldContainer();
        objectsAdder = new ObjectsAdder(worldContainer, this, objectCounter);

        AppInitializer.isRunning = true;
    }

    /**
     * Remove safely all threads in simulation
     */
    public void removeThreads() {
        AppInitializer.isRunning = false;

        while (!worldContainer.getAllThreads().isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initialize threads from all Trader being currently in simulation
     */
    public void addThreads() {
        AppInitializer.isRunning = true;
        for (Trader trader : worldContainer.getAllTraders()) {
            System.out.println("Starting thread for the second time");
            Thread t = new Thread(trader);
            worldContainer.addNewThread(trader.getName(), t);
            t.start();
        }
    }

    /**
     * After loading save we need to set some propertias that are not Serializable
     */
    public void setAllProperties() {
        for (Asset asset : worldContainer.getAllAssets()) {
            asset.setProperties();
        }
        for (Trader trader : worldContainer.getAllTraders()) {
            trader.setProperties();
        }
    }

    /**
     * Before serialization we need to save some parameters that are not
     * serializable to Arrays that are serializable.
     * 
     * @param controller
     */
    public void readAllProperties(MainPanelController controller) {
        for (Asset asset : worldContainer.getAllAssets()) {
            asset.readProperties();
        }
        for (Trader trader : worldContainer.getAllTraders()) {
            trader.readProperties();
        }
        for (InvestmentFund fund : worldContainer.getInvestmentFunds()) {
            fund.setController(controller);
        }
    }
}
