package market.gui;

import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import market.assets.Asset;
import market.world.World;

/**
 * Controller for plotting different Assets
 */
public class AssetPlotController implements Initializable {

    private LinkedList<String> plottedSeries = new LinkedList<String>();
    private String scale = "normal";
    private World world;
    private Asset firstAsset;
    @FXML
    private LineChart<Number, Number> assetValueChart;
    @FXML
    private Button changeScale;
    @FXML
    private ListView<String> assetsListView;

    private ObservableList<String> assetsNames = FXCollections.observableArrayList();

    /**
     * Just sets first asset to be plotted
     * 
     * @param asset asset to be plotted
     * @param world needed to be able to choose more assets
     */
    public AssetPlotController(Asset asset, World world) {
        this.firstAsset = asset;
        this.world = world;

    }

    /**
     * Functions initializing all event that may happen in that controller
     * 
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        assetValueChart.setAnimated(false); // ! For removing Plots to work!!!
        // POPULATING LIST VIEW!
        this.assetsNames.addAll(world.getWorldContainer().getAllAssetsNames());

        assetsListView.setItems(assetsNames);
        // PLOTTING EVERYTHING
        plottedSeries.add(firstAsset.getName());
        replotAllAssets();

        assetsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String assetName = assetsListView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2 && !Objects.isNull(assetName)) {
                    // System.out.println(assetName);
                    int indexOfAsset = plottedSeries.indexOf(assetName);
                    if (indexOfAsset != -1) {
                        plottedSeries.remove(indexOfAsset);
                        replotAllAssets();
                    } else {
                        plottedSeries.addLast(assetName);
                        replotAllAssets();
                    }
                }
            }
        });

        changeScale.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (scale.equals("normal")) {
                    changeScale.setText("normal");
                    scale = "percentage";
                    replotAllAssets();
                } else {
                    changeScale.setText("percentage");
                    scale = "normal";
                    replotAllAssets();
                }
            }
        });
    }

    /**
     * Function for replotting All assets. It is needed as different assets may have
     * not the same amount of rates changes. So we must recalculate the offsets. So
     * that plot looks good
     */
    public void replotAllAssets() {
        int longestPlot = 0;
        for (String asset : plottedSeries) {
            int numberOfStoredRates = world.getParticularAsset(asset).getMainBankRates().getNumberOfStoredRates();
            if (numberOfStoredRates > longestPlot) {
                longestPlot = numberOfStoredRates;
            }
        }
        assetValueChart.getData().clear();
        for (String asset : plottedSeries) {
            plotAsset(world.getParticularAsset(asset), longestPlot);
        }

    }

    /**
     * Function adding given asset to the controller's plot
     * 
     * @param asset
     * @param longestPlot to know how to set the offset of the ticks
     */
    public void plotAsset(Asset asset, int longestPlot) {
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.setName(asset.getName());
        asset.getMainBankRates().fillAssetSeries(series, this.scale, longestPlot);

        assetValueChart.getData().add(series);
    }

    public void clearPlot() {
        plottedSeries.clear();
        replotAllAssets();
    }

}
