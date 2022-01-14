package market.gui;

import java.net.URL;
import java.util.ResourceBundle;

import market.traders.Company;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import java.util.Map;

import market.traders.Trader;

/**
 * Controller for showing additional information about trader
 */
public class TraderDetailsController implements Initializable {

  private Trader trader;

  @FXML
  private TableView<Map.Entry<String, Float>> tableView;
  @FXML
  private TableColumn<Map.Entry<String, Float>, String> assetName;
  @FXML
  private TableColumn<Map.Entry<String, Float>, String> amount;
  @FXML
  private GridPane traderDetails;
  @FXML
  private Button shareButton;
  @FXML
  private Spinner<Integer> shareSpinner;
  private ObservableList<Map.Entry<String, Float>> dataList = FXCollections.observableArrayList();

  /**
   * 
   * @param trader Informations about given trader will be displayed in a separate
   *               stage
   */
  public TraderDetailsController(Trader trader) {
    this.trader = trader;
    dataList.addAll(this.trader.getInvestmentBudget().entrySet());
  }

  /**
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    trader.fillGridPane(traderDetails);

    // ! Filling TableView with elements from HashMap is a little bit trickier
    assetName.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Float>, String>, ObservableValue<String>>() {

          @Override
          public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Float>, String> p) {
            // this callback returns property for just one cell, you can't use a loop here
            // for first column we use key
            return new SimpleStringProperty(p.getValue().getKey());
          }
        });

    amount.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Float>, String>, ObservableValue<String>>() {

          @Override
          public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Float>, String> p) {
            // this callback returns property for just one cell, you can't use a loop here
            // for first column we use key
            return new SimpleStringProperty(p.getValue().getValue().toString());
          }
        });

    tableView.setItems(dataList);

    // Additionally if trader is Company we add some additional button
    if (this.trader.getType().equals("Company")) {
      shareButton.setVisible(true);
      shareButton.setDisable(false);
      shareSpinner.setVisible(true);
      shareSpinner.setDisable(false);
      shareSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0, 1));
    }

  }

  public void decreaseCompanyShares() {
    Company company = (Company) trader;
    company.decreaseShares(shareSpinner.getValue());
  }
}
