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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import market.traders.*;
import java.util.ArrayList;
import market.App;


public class TradersPanelController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML private TextField filterField;
    @FXML private TableView<Trader> tableView;
    @FXML private TableColumn<Trader, String> traderId;
    @FXML private TableColumn<Trader, String> traderName;
    @FXML private TableColumn<Trader, String> traderType;
    @FXML private TableColumn<Trader, Float> traderBudgetInGold;

    private ObservableList<Trader> dataList = FXCollections.observableArrayList();

    public void addTraders(ArrayList<Trader> traders){
        this.dataList.addAll(traders);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //This PropertyValueFactory will seek for a getters inside the Class I provided earlier
        traderId.setCellValueFactory(new PropertyValueFactory<>("tradingIdentifier")); // Now I must create Corresponding getters getName etc...
        traderName.setCellValueFactory(new PropertyValueFactory<>("name"));
        traderType.setCellValueFactory(new PropertyValueFactory<>("type"));
        traderBudgetInGold.setCellValueFactory(new PropertyValueFactory<>("budgetInGold"));

        tableView.setRowFactory(tv -> {
            TableRow<Trader> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Trader rowData = row.getItem();
                    try {
                        popUpInformationAboutTrader(rowData);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            
                }
            });
            return row ;
        });

        App.passTradersToController(this);
        // java lambdas: (arguments) -> body
        //! The filtering part I've just taken from tutorial.
        //Wrap the ObservableList in a FilteredList (initially display all data) since at the beginning predicate is always true.
        FilteredList<Trader> filteredData = new FilteredList<>(dataList, b -> true);

        //Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(trader -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (trader.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (trader.getType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (trader.getTradingIdentifier().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Trader> sortedData = new SortedList<>(filteredData);
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

    public void popUpInformationAboutTrader(Trader trader) throws IOException{
        // if (trader.getType().equals("human investor")){
        //     FXMLLoader loader = new FXMLLoader(TadersPanelController.class.getResource(""))
        // }
        FXMLLoader loader =  new FXMLLoader(TradersPanelController.class.getResource("traderDetailsScene.fxml"));
        
        TraderDetailsController controller = new TraderDetailsController(trader);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
