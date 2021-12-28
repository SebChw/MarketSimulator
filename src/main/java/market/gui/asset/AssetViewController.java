package market.gui.asset;

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
import market.assets.Asset;
import java.util.ArrayList;
import market.App;

public class AssetViewController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML private TextField filterField;
    @FXML private TableView<Asset> tableView;
    @FXML private TableColumn<Asset, String> assetName;
    @FXML private TableColumn<Asset, String> assetType;
    @FXML private TableColumn<Asset, Float> assetCirculation;
    @FXML private TableColumn<Asset, Integer> assetHype;
    @FXML private TableColumn<Asset, Integer> assetOwners;

    private ObservableList<Asset> dataList = FXCollections.observableArrayList();

    public void addAssets(ArrayList<Asset> assets){
        this.dataList.addAll(assets);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assetName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assetType.setCellValueFactory(new PropertyValueFactory<>("type"));
        assetCirculation.setCellValueFactory(new PropertyValueFactory<>("amountInCirculation"));
        assetHype.setCellValueFactory(new PropertyValueFactory<>("hypeLevel"));
        assetOwners.setCellValueFactory(new PropertyValueFactory<>("amountOfOwners"));

        App.passAssetsToController(this);
        // java lambdas: (arguments) -> body
        //! The filtering part I've just taken from tutorial.
        //Wrap the ObservableList in a FilteredList (initially display all data) since at the beginning predicate is always true.
        FilteredList<Asset> filteredData = new FilteredList<>(dataList, b -> true);

        //Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(asset -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (asset.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (asset.getType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Asset> sortedData = new SortedList<>(filteredData);
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
