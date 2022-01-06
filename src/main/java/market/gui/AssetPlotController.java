package market.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public class AssetPlotController implements Initializable {

    //I don't know how to have always the same color when deleting series
    //With HashSet Colors are changing during replotting
    //Lets try List. It's only hundreds of 
    //private HashSet<String> plottedSeries = new HashSet<String>();//! Probablt we wont have more than 20 ? assets plotted so this seems like overkill
    //I need this yet! when removing series I should have it's index!
    private LinkedList<String> plottedSeries = new LinkedList<String>();
    private String scale = "normal";
    private World world;
    private Asset firstAsset;
    //private volatile int plottedAssetsNumber = 0;
    @FXML private LineChart<Number,Number> assetValueChart;
    @FXML private Button changeScale;
    @FXML private ListView<String> assetsListView;


    private ObservableList<String> assetsNames = FXCollections.observableArrayList();

    public AssetPlotController(Asset asset, World world){
        this.firstAsset = asset;
        this.world = world;
        
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        assetValueChart.setAnimated(false); //! For removing Plots to work!!!
        //POPULATING LIST VIEW!
        this.assetsNames.addAll(world.getAllAssetsNames());
        
        assetsListView.setItems(assetsNames);
        //PLOTTING EVERYTHING
        plottedSeries.add(firstAsset.getName());
        replotAllAssets();
        

        assetsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String assetName = assetsListView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2 && !Objects.isNull(assetName)){
                    //System.out.println(assetName);
                    int indexOfAsset = plottedSeries.indexOf(assetName);
                    if(indexOfAsset != -1){
                        plottedSeries.remove(indexOfAsset);
                        replotAllAssets();
                    }
                    else {
                        plottedSeries.addLast(assetName);
                        replotAllAssets();
                    }   
                }
            }
        });

        changeScale.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if (scale.equals("normal")){
                    changeScale.setText("normal");
                    scale = "percentage";
                    replotAllAssets();
                }
                else{
                    changeScale.setText("percentage");
                    scale = "normal";
                    replotAllAssets();
                }
            }
        });
    }

    //Every Time new asset is added we replot all plots this seems to work best!
    public void replotAllAssets(){
        int longestPlot = 0;
        for (String asset : plottedSeries) {
            int numberOfStoredRates = world.getParticularAsset(asset).getMainBankRates().getNumberOfStoredRates();
            if ( numberOfStoredRates > longestPlot){
                longestPlot = numberOfStoredRates;
            }
        }
        assetValueChart.getData().clear();
        for (String asset : plottedSeries) {
            plotAsset(world.getParticularAsset(asset), longestPlot);
        }
        
    }

    public void plotAsset(Asset asset, int longestPlot){
        XYChart.Series series = new XYChart.Series();
        series.setName(asset.getName());
        asset.getMainBankRates().fillAssetSeries(series, this.scale, longestPlot);

        assetValueChart.getData().add(series);
    }

    // public void removeAsset(Asset asset){
    //     XYChart.Series series = plottedSeries.get(asset.getName());
    //     assetValueChart.getData().remove(series);
    //     plottedSeries.remove(asset.getName());

    //     replotAllAssets();
    // }
    
}
