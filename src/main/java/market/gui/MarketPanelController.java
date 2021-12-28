package market.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import market.markets.Market;
import java.util.ArrayList;
import market.App;
import market.assets.Asset;

public class MarketPanelController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML private TextField filterField;
    @FXML private TableView<Market<? extends Asset>> tableView;
    @FXML private TableColumn<Market<? extends Asset>, String> marketName;
    @FXML private TableColumn<Market<? extends Asset>, String> marketType;
    @FXML private TableColumn<Market<? extends Asset>, String> marketCountry;
    @FXML private TableColumn<Market<? extends Asset>, String> marketCity;
    @FXML private TableColumn<Market<? extends Asset>, String> marketAddress;
    @FXML private TableColumn<Market<? extends Asset>, Float> marketCost;
    @FXML private TableColumn<Asset, String> marketCurrency;

    private ObservableList<Market<? extends Asset>> dataList = FXCollections.observableArrayList();

    public void addMarkets(ArrayList<Market<? extends Asset>> markets){
        this.dataList.addAll(markets);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //This PropertyValueFactory will seek for a getters inside the Class I provided earlier
        marketName.setCellValueFactory(new PropertyValueFactory<>("name")); // Now I must create Corresponding getters getName etc...
        marketType.setCellValueFactory(new PropertyValueFactory<>("AssetType"));
        marketCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        marketCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        marketAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        marketCost.setCellValueFactory(new PropertyValueFactory<>("percentageOperationCost"));
        marketCurrency.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        App.passMarketsToController(this);
        // java lambdas: (arguments) -> body
        //! The filtering part I've just taken from tutorial.
        //Wrap the ObservableList in a FilteredList (initially display all data) since at the beginning predicate is always true.
        FilteredList<Market<? extends Asset>> filteredData = new FilteredList<>(dataList, b -> true);

        //Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(market -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (market.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getAssetType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getCountry().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getCurrencyName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Market<?>> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    public void switchToMainMenu(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(App.class.getResource("MainWindow.fxml"));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
