package market.gui;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import market.assets.Asset;
import market.entityCreator.SemiRandomValuesGenerator;
import market.markets.Market;
import market.traders.Trader;
import market.world.World;

/**
 * Class used to fill TableViews with different kind of data
 */
public class TableFiller {
    private World world;

    private String[] marketColumnsNames = { "Name", "Type", "Country", "City", "Address", "Cost", "Currency" };
    private String[] marketConstructorNames = { "name", "AssetType", "country", "city", "address",
            "percentageOperationCost", "currencyName" };
    private ObservableList<TableColumn<Market, String>> marketColumns = FXCollections.observableArrayList();

    private String[] traderColumnsNames = { "Identifier", "Name", "Type", "Budget In Gold" };
    private String[] traderConstructorNames = { "tradingIdentifier", "name", "type", "budgetInGold" };
    private ObservableList<TableColumn<Trader, String>> traderColumns = FXCollections.observableArrayList();

    private String[] assetColumnsNames = { "Name", "Type", "# in Circulation", "# Hype", "# Onwers" };
    private String[] assetConstructorNames = { "name", "type", "amountInCirculation", "hypeLevel", "amountOfOwners" };

    private ObservableList<TableColumn<Asset, String>> assetColumns = FXCollections.observableArrayList();
    private MainPanelController controller;

    public TableFiller(World world, MainPanelController controller) {
        this.world = world;
        this.controller = controller;
    }

    /**
     * Fill for the first time market Table and sets all cell factories
     * 
     * @param table
     * @param marketsData FilteredList with market data
     */
    public void fillMarketTable(TableView<Market> table, FilteredList<Market> marketsData) {
        for (int i = 0; i < marketColumnsNames.length; i++) {
            TableColumn<Market, String> column = new TableColumn<Market, String>(marketColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(marketConstructorNames[i]));
            marketColumns.add(column);
        }
        table.getColumns().addAll(marketColumns);
        table.setItems(marketsData);

        setClickEvent(table, "market");
    }

    /**
     * Fill for the first time trader table view and set all cell factories
     * 
     * @param table
     * @param tradersData
     */
    public void fillTraderTable(TableView<Trader> table, FilteredList<Trader> tradersData) {
        for (int i = 0; i < traderColumnsNames.length; i++) {
            TableColumn<Trader, String> column = new TableColumn<Trader, String>(traderColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(traderConstructorNames[i]));
            traderColumns.add(column);
        }
        table.getColumns().addAll(traderColumns);
        table.setItems(tradersData);

        setClickEvent(table, "trader");
    }

    /**
     * @param table
     * @param assetData
     */
    public void fillAssetTable(TableView<Asset> table, FilteredList<Asset> assetData) {
        // !Tell a story about how you tried hard to add Tooltip and new scene factory
        // here and then checked what setClickEvent Does!
        for (int i = 0; i < assetColumnsNames.length; i++) {
            TableColumn<Asset, String> column = new TableColumn<Asset, String>(assetColumnsNames[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(assetConstructorNames[i]));
            assetColumns.add(column);
        }
        table.getColumns().addAll(assetColumns);
        table.setItems(assetData);

        setClickEvent(table, "asset");
    }

    /**
     * @param root
     */
    private void popUp(Parent root, Object controller, String type) {
        Image icon = new Image(TableFiller.class.getResourceAsStream("coin.png")); // this url is taken from javaFX
                                                                                   // docs
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
        if (type.equals("asset")) {
            AssetPlotController c = (AssetPlotController) controller;
            stage.setOnCloseRequest(e -> {
                c.stop();
            });
        }

        stage.show();
    }

    private void popUpInformationAbout(Object thing, String type) throws IOException {
        // ! That's probably not the best way to solve this
        FXMLLoader loader;
        String filename;
        if (type.equals("asset"))
            filename = "assetPlotScene.fxml";
        else if (type.equals("trader"))
            filename = "traderDetailsScene.fxml";
        else
            filename = "marketDetailsScene.fxml";

        loader = new FXMLLoader(TableFiller.class.getResource(filename));
        loadController(loader, type, thing);
        Parent root = loader.load();
        popUp(root, loader.getController(), type);
    }

    private void loadController(FXMLLoader loader, String type, Object thing) {
        if (type.equals("asset"))
            loader.setController(new AssetPlotController((Asset) thing, world,
                    SemiRandomValuesGenerator.getRandomIdentifier(10), this.controller));
        else if (type.equals("trader"))
            loader.setController(new TraderDetailsController((Trader) thing));
        else
            loader.setController(new MarketDetailsController((Market) thing));
    }

    /**
     * Sets events happening when interacting with the table view rows
     * 
     * @param table
     * @param type  whether tableView contains Assets, Traders or Markets
     */
    public <T> void setClickEvent(TableView<T> table, String type) {

        table.setRowFactory(tv -> {
            // !Be Carefull here. Thius may be buggy?
            TooltipTableRow<T> row = new TooltipTableRow<T>((T thing) -> {
                return thing.toString();
            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    try {
                        popUpInformationAbout(rowData, type);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            });

            row.setOnMouseEntered(event -> {
                T rowData = row.getItem();
                row.setTooltipText(rowData);
            });
            return row;
        });
    }

}
