package market.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import market.assets.Asset;
import market.markets.Market;
import market.traders.Trader;
import market.world.World;
import market.traders.Company;
import market.traders.HumanInvestor;

public class MainPanelController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root; //The base class for all nodes that have children in the scene graph
    private World world;

    //WE MUST SPECIFY IDS FOR THE CONTAINERS!!!! THAT ARE NESTED!!
    @FXML private Label dateLabel;
    @FXML private TextField assetSearch;
    @FXML private TextField marketSearch;
    @FXML private TextField traderSearch;
    @FXML private Label assetLabel;
    @FXML private Label marketLabel;
    @FXML private Label traderLabel;
    @FXML private GridPane centerPane;
    @FXML private TableView<Asset> assetTable;
    @FXML private TableView<Market> marketTable;
    @FXML private TableView<Trader> traderTable;
    @FXML private GridPane worldDetails;
    
    private ObservableList<Market> marketsData = FXCollections.observableArrayList();
    private FilteredList<Market> filteredMarkets = new FilteredList<>(marketsData, b -> true);
    private ObservableList<Asset> assetsData = FXCollections.observableArrayList();
    private FilteredList<Asset> filteredAssets = new FilteredList<>(assetsData, b -> true);
    private ObservableList<Trader> tradersData = FXCollections.observableArrayList();
    private FilteredList<Trader> filteredTraders = new FilteredList<>(tradersData, b -> true);

    private TableFiller tableFiller;

    private  boolean assetSeen = false;
    private  boolean marketSeen = false;
    private  boolean traderSeen = false;
    
    private TableFilter tableFilter = new TableFilter();

    public MainPanelController(World world){
        this.world=world;
        this.tableFiller=new TableFiller(world);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dateLabel.setText("Current Date: " + world.getCurrentTime().toString());
        
        world.getObjectCounter().fillGridPane(worldDetails);
        world.addNewCurrency();
        world.addNewCommodity();
        world.addNewCompany();
        Market market = world.addNewRandomMarket();
        this.marketsData.addAll(world.getAllMarkets());
        this.assetsData.addAll(world.getAllAssets());
        this.tradersData.addAll(world.getAllTraders());
        tableFiller.fillMarketTable(marketTable, filteredMarkets);
        tableFilter.marketFilter(marketSearch, filteredMarkets);
        tableFiller.fillAssetTable(assetTable, filteredAssets);
        tableFilter.assetFilter(assetSearch, filteredAssets);
        tableFiller.fillTraderTable(traderTable, filteredTraders);
        tableFilter.traderFilter(traderSearch, filteredTraders);
        
    }

    public void updateDate(){
        world.addOneDay();
        //System.out.println(world.getCurrentTime().toString());
        dateLabel.setText("Current Date: " + world.getCurrentTime().toString());
    }

    public void setVisible(int index, TextField text, TableView<?> table, Label label){
        centerPane.getColumnConstraints().get(index).setMaxWidth(2000);
        text.setVisible(true);
        table.setVisible(true);
        label.setVisible(true);
    }
    public void setInvisible(int index, TextField text, TableView<?> table, Label label){
        centerPane.getColumnConstraints().get(index).setMaxWidth(0);
        text.setVisible(false);
        table.setVisible(false);
        label.setVisible(false);
    }

    public void refreshTable(TableView<?>table){
        marketTable.getColumns().get(0).setVisible(false);
        marketTable.getColumns().get(0).setVisible(true); 
    }

    public void addMarket(){
        marketsData.add(world.addNewRandomMarket());
        refreshTable(marketTable);   
    }

    public void addCurrency(){
        assetsData.add(world.addNewCurrency());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addStaticIndex(){
        assetsData.add(world.addNewMarketIndex());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }
    public void addDynamicIndex(){
        assetsData.add(world.addNewDynamicMarketIndex());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }
    public void addCommodity(){
        assetsData.add(world.addNewCommodity());
        refreshTable(assetTable);
        world.checkNeedOfCreatingInvestor(this);
    }

    public void addCompany(){
        Company company =world.addNewCompany();
        tradersData.add(company);
        assetsData.add(company.getShare());
        refreshTable(traderTable);
        refreshTable(assetTable);
    }
    public void addHumanInvestor(){
        tradersData.add(world.addNewHumanInvestor());
        refreshTable(traderTable);
       
    }
    public void addInvestmentFund(){
        tradersData.add(world.addNewInvestmentFund());
        refreshTable(traderTable);
    }

    public void showAssets(){
        if(assetSeen) {
            setVisible(0, assetSearch, assetTable,assetLabel);
        } else {
            setInvisible(0, assetSearch, assetTable,assetLabel);
        }

        assetSeen = !assetSeen;
    }
    public void showMarkets(){
        if(marketSeen) {
            setVisible(1, marketSearch, marketTable,marketLabel);
        } else {
            setInvisible(1, marketSearch, marketTable,marketLabel);
        }

        marketSeen = !marketSeen;
    }

    public void showTraders(){
        if(traderSeen) {
            setVisible(2, traderSearch, traderTable, traderLabel);
        } else {
            setInvisible(2, traderSearch, traderTable, traderLabel);
        }

        traderSeen = !traderSeen;
    }

}
