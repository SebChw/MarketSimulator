package market.world;

import market.AppInitializer;
import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;
import javafx.application.Platform;

/**
 * Thread responsible for simulating the "time" in the simulation.
 */
public class WorldThread extends Thread {

    private World world;
    private MainPanelController controller;

    /**
     * @param world      world on which Simulation is running
     * @param controller main controller that is seen by the user
     */
    public WorldThread(World world, MainPanelController controller) {
        this.world = world;
        this.controller = controller;
    }

    /**
     * In constant time interval increase date by one and also at the end of each
     * day updates all rates and dynamic indices
     */
    @Override
    public void run() {
        Runnable updater = new Runnable() {

            @Override
            public void run() {
                // ! If any thread tries to update sth with JAVA FX WE NEED to run it via the
                // Platform.runLater
                controller.updateDate();
                if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.1
                        && !world.getWorldContainer().getInvestmentFunds().isEmpty()) {
                    SemiRandomValuesGenerator.getRandomInvestmentFund(world.getWorldContainer().getInvestmentFunds())
                            .issueFundUnit();
                }
            }
        };

        while (AppInitializer.isRunning) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }

            // UI update is run on the Application thread
            world.updateAllRates();
            // world.updateAllIndices();
            Platform.runLater(updater);
        }
    }
}
