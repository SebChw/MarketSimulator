package market.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import market.assets.Asset;
import market.markets.Market;

public class MarketDetailsController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Market market;

    @FXML private TableView<Asset> tableView;
    @FXML private TableColumn<Asset, String> assetName;
    @FXML private TableColumn<Asset, String> assetType;
    @FXML private GridPane marketDetails;

    private ObservableList<Asset> dataList = FXCollections.observableArrayList();

    public MarketDetailsController(Market market){
        this.market = market;
        dataList.addAll(market.getAvailableAssets());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          market.fillGridPane(marketDetails);

          assetName.setCellValueFactory(new PropertyValueFactory<>("name"));
          assetType.setCellValueFactory(new PropertyValueFactory<>("type"));

          tableView.setItems(dataList);

    }

}
