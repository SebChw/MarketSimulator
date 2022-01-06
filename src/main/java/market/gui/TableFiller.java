package market.gui;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import market.assets.Asset;
import market.markets.Market;
import market.traders.Trader;
import market.world.World;
public class TableFiller {
    private World world;

    private String [] marketColumnsNames = {"Name", "Type", "Country", "City", "Address", "Cost", "Currency"};
    private String [] marketConstructorNames = {"name", "AssetType", "country", "city", "address", "percentageOperationCost", "currencyName"};
    private ObservableList<TableColumn<Market, String>> marketColumns = FXCollections.observableArrayList();

    private String [] traderColumnsNames = {"Identifier", "Name", "Type", "Budget In Gold"};
    private String [] traderConstructorNames = {"tradingIdentifier", "name", "type", "budgetInGold"};
    private ObservableList<TableColumn<Trader, String>> traderColumns = FXCollections.observableArrayList();

    private String [] assetColumnsNames = {"Name", "Type", "# in Circulation", "# Hype", "# Onwers"};
    private String [] assetConstructorNames = {"name", "type", "amountInCirculation", "hypeLevel", "amountOfOwners"};

    private ObservableList<TableColumn<Asset, String>> assetColumns = FXCollections.observableArrayList();

    public TableFiller(World world){
        this.world = world;
    }

    public void fillMarketTable(TableView<Market> table, FilteredList<Market> marketsData){
        for (int i = 0; i < marketColumnsNames.length; i++){
            TableColumn<Market,String> column = new TableColumn<Market,String>(marketColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(marketConstructorNames[i]));
            marketColumns.add(column);
        }
        table.getColumns().addAll(marketColumns);
        table.setItems(marketsData);

        setClickEvent(table, "market");
    }

    public void fillTraderTable(TableView<Trader> table, FilteredList<Trader> marketsData){
        for (int i = 0; i < traderColumnsNames.length; i++){
            TableColumn<Trader,String> column = new TableColumn<Trader,String>(traderColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(traderConstructorNames[i]));
            traderColumns.add(column);
        }
        table.getColumns().addAll(traderColumns);
        table.setItems(marketsData);

        setClickEvent(table, "trader");
    }

    public void fillAssetTable(TableView<Asset> table, FilteredList<Asset> marketsData){
        for (int i = 0; i < assetColumnsNames.length; i++){
            TableColumn<Asset,String> column = new TableColumn<Asset,String>(assetColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(assetConstructorNames[i]));
            assetColumns.add(column);
        }
        table.getColumns().addAll(assetColumns);
        table.setItems(marketsData);

        setClickEvent(table, "asset");
    }

    public void popUp(Parent root) {
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void popUpInformationAbout(Object thing, String type) throws IOException{
        FXMLLoader loader;
        if (type.equals("asset")){
            loader =  new FXMLLoader(TableFiller.class.getResource("assetPlotScene.fxml"));
            AssetPlotController controller = new AssetPlotController((Asset)thing, world);
            loader.setController(controller);
        }
        else if(type.equals("trader")){
            loader =  new FXMLLoader(TableFiller.class.getResource("traderDetailsScene.fxml"));
            TraderDetailsController controller = new TraderDetailsController((Trader)thing);
            loader.setController(controller);
        }
        else{
            loader =  new FXMLLoader(TableFiller.class.getResource("MarketDetailsScene.fxml"));
            MarketDetailsController controller = new MarketDetailsController((Market)thing);
            loader.setController(controller);
        }

        Parent root = loader.load();
        popUp(root);
        
    }

    public <T> void setClickEvent(TableView<T> table, String type){
        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    T rowData = row.getItem();
                    try {
                        popUpInformationAbout(rowData, type);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            
                }
            });
            return row ;
        });
    }

}
