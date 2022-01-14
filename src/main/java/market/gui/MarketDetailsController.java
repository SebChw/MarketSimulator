package market.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import market.assets.Asset;
import market.markets.Market;

/**
 * Controller for showing additional informations about Market
 */
public class MarketDetailsController implements Initializable {

    private Market market;

    @FXML
    private TableView<Asset> tableView;
    @FXML
    private TableColumn<Asset, String> assetName;
    @FXML
    private TableColumn<Asset, String> assetType;
    @FXML
    private GridPane marketDetails;

    private ObservableList<Asset> dataList = FXCollections.observableArrayList();

    /**
     * 
     * @param market Information about passed market will be displayed in separate
     *               Stage
     */
    public MarketDetailsController(Market market) {
        this.market = market;
        dataList.addAll(market.getAvailableAssets());
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        market.fillGridPane(marketDetails);

        assetName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assetType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.setItems(dataList);

    }

}
