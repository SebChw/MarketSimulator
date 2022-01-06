package market.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.Map;

import market.App;

import market.traders.Trader;
import market.world.World;

public class TraderDetailsController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Trader trader;

    @FXML private TableView<Map.Entry<String, Float>> tableView;
    @FXML private TableColumn<Map.Entry<String, Float>, String> assetName;
    @FXML private TableColumn<Map.Entry<String, Float>, String> amount;
    @FXML private GridPane traderDetails;
    private ObservableList<Map.Entry<String, Float>> dataList = FXCollections.observableArrayList();
   
    public TraderDetailsController(Trader trader){
        this.trader = trader;
        dataList.addAll(this.trader.getInvestmentBudget().entrySet());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          trader.fillGridPane(traderDetails);
          
          assetName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Float>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Float>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey());
            }
          });

          amount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Float>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Float>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getValue().toString());
            }
          });


          tableView.setItems(dataList);

    }
}
