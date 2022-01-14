package market.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import market.assets.Asset;
import market.assets.InvestmentFundUnit;
import market.markets.Market;
import market.traders.Trader;
import market.world.World;
import market.traders.Company;

/**
 * Main controller of the entire application
 * All methods with add prefix are self explanatory
 * methods with Show are used to show/hide particular TableViews
 */
public class MainPanelController implements Initializable {
    private World world;

    // WE MUST SPECIFY IDS FOR THE CONTAINERS!!!! THAT ARE NESTED!!
    @FXML
    private Label dateLabel;
    @FXML
    private TextField assetSearch;
    @FXML
    private TextField marketSearch;
    @FXML
    private TextField traderSearch;
    @FXML
    private Label assetLabel;
    @FXML
    private Label marketLabel;
    @FXML
    private Label traderLabel;
    @FXML
    private GridPane centerPane;
    @FXML
    private TableView<Asset> assetTable;
    @FXML
    private TableView<Market> marketTable;
    @FXML
    private TableView<Trader> traderTable;
    @FXML
    private GridPane worldDetails;
    @FXML
    private Slider transactionProbabilitySlider;
    @FXML
    private Label transactionProbabilityLabel;
    @FXML
    private Slider bullProbabilitySlider;
    @FXML
    private Label bullProbabilityLabel;

    private ObservableList<Market> marketsData = FXCollections.observableArrayList();
    private FilteredList<Market> filteredMarkets = new FilteredList<>(marketsData, b -> true);
    private ObservableList<Asset> assetsData = FXCollections.observableArrayList();
    private FilteredList<Asset> filteredAssets = new FilteredList<>(assetsData, b -> true);
    private ObservableList<Trader> tradersData = FXCollections.observableArrayList();
    private FilteredList<Trader> filteredTraders = new FilteredList<>(tradersData, b -> true);

    private TableFiller tableFiller;

    private boolean assetSeen = true;
    private boolean marketSeen = true;
    private boolean traderSeen = true;

    private TableFilter tableFilter = new TableFilter();

    /**
     * 
     * @param world Main world object in the simulation
     */
    public MainPanelController(World world) {
        this.world = world;
        this.tableFiller = new TableFiller(world);
    }

    /**
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dateLabel.setText("Current Date: " + world.getCurrentTime().toString());

        world.getObjectCounter().fillGridPane(worldDetails);
        world.getObjectsAdder().addNewCurrency();
        world.getObjectsAdder().addNewCommodity();
        // world.getObjectsAdder().addNewInvestmentFund(this);
        // world.getObjectsAdder().addNewCompany();
        // Market market = world.getObjectsAdder().addNewRandomMarket();
        this.marketsData.addAll(world.getWorldContainer().getAllMarkets());
        this.assetsData.addAll(world.getWorldContainer().getAllAssets());
        this.tradersData.addAll(world.getWorldContainer().getAllTraders());
        tableFiller.fillMarketTable(marketTable, filteredMarkets);
        tableFilter.searchableObjectsFilter(marketSearch, filteredMarkets);
        tableFiller.fillAssetTable(assetTable, filteredAssets);
        tableFilter.searchableObjectsFilter(assetSearch, filteredAssets);
        tableFiller.fillTraderTable(traderTable, filteredTraders);
        tableFilter.searchableObjectsFilter(traderSearch, filteredTraders);

        transactionProbabilityLabel.textProperty().bind(
                Bindings.format(
                        "transaction probability: %.2f",
                        transactionProbabilitySlider.valueProperty()));

        bullProbabilityLabel.textProperty().bind(
                Bindings.format("bull probability: %.2f", bullProbabilitySlider.valueProperty()));

        transactionProbabilitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                world.setTransactionProbability(newValue.floatValue());
            }
        });

        bullProbabilitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                world.setBullProbability(newValue.floatValue());
            }
        });

    }

    /**
     * Function increasing the "time" of the simulation
     */
    public void updateDate() {
        world.addOneDay();
        dateLabel.setText("Current Date: " + world.getCurrentTime().toString());
    }

    private void setVisible(int index, TextField text, TableView<?> table, Label label) {
        centerPane.getColumnConstraints().get(index).setMaxWidth(0);
        text.setVisible(false);
        table.setVisible(false);
        label.setVisible(false);
    }

    private void setInvisible(int index, TextField text, TableView<?> table, Label label) {
        centerPane.getColumnConstraints().get(index).setMaxWidth(2000);
        text.setVisible(true);
        table.setVisible(true);
        label.setVisible(true);
    }

    private void refreshTable(TableView<?> table) {
        marketTable.getColumns().get(0).setVisible(false);
        marketTable.getColumns().get(0).setVisible(true);

    }

    public void addMarket() {
        marketsData.add(world.getObjectsAdder().addNewRandomMarket());
        refreshTable(marketTable);
    }

    public void addCurrency() {
        assetsData.add(world.getObjectsAdder().addNewCurrency());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addStaticIndex() {
        assetsData.add(world.getObjectsAdder().addNewMarketIndex());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addDynamicIndex() {
        assetsData.add(world.getObjectsAdder().addNewDynamicMarketIndex());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addCommodity() {
        assetsData.add(world.getObjectsAdder().addNewCommodity());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addCompany() {
        Company company = world.getObjectsAdder().addNewCompany();
        tradersData.add(company);
        assetsData.add(company.getShare());
        refreshTable(traderTable);
        refreshTable(assetTable);
    }

    public void addHumanInvestor() {
        tradersData.add(world.getObjectsAdder().addNewHumanInvestor());
        refreshTable(traderTable);

    }

    public void addInvestmentFund() {
        tradersData.add(world.getObjectsAdder().addNewInvestmentFund(this));
        refreshTable(traderTable);
    }

    /**
     * @param unit unit to be added to the world
     */
    public void addInvestmentFundUnit(InvestmentFundUnit unit) {
        assetsData.add(unit);
        refreshTable(traderTable);
    }

    public void showAssets() {
        if (assetSeen) {
            setVisible(0, assetSearch, assetTable, assetLabel);
        } else {
            setInvisible(0, assetSearch, assetTable, assetLabel);
        }

        assetSeen = !assetSeen;
    }

    public void showMarkets() {
        if (marketSeen) {
            setVisible(1, marketSearch, marketTable, marketLabel);
        } else {
            setInvisible(1, marketSearch, marketTable, marketLabel);
        }

        marketSeen = !marketSeen;
    }

    public void showTraders() {
        if (traderSeen) {
            setVisible(2, traderSearch, traderTable, traderLabel);
        } else {
            setInvisible(2, traderSearch, traderTable, traderLabel);
        }

        traderSeen = !traderSeen;
    }

}
