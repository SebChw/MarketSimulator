package market.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import market.App;
import javafx.scene.Node;

public class ControlPanelController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Label currencyLabel;
    @FXML private Label commodityLabel;
    @FXML private Label marketLabel;
    @FXML private Label indexLabel;
    @FXML private Label companyLabel;
    @FXML private Label humanInvestorLabel;
    @FXML private Label investmentFundLabel;

    
    public void createMarket(){
        App.addMarket();
        marketLabel.setText("Markets: " + App.getNumberOfMarkets());
    }

    public void createCurrency(){
        App.addCurrency();
        currencyLabel.setText("Currency: " + App.getNumberOfCurrencies());
    }

    public void createCommodity(){
        App.addCommodity();
        commodityLabel.setText("Commodity: " + App.getNumberOfCommodities());
    }

    public void createIndex(){

    }

    public void createCompany(){
        App.addCompany();
        companyLabel.setText("Companies: " + App.getNumberOfCompanies());
    }
    public void createHumanInvestor(){
        App.addHumanInvestor();
        humanInvestorLabel.setText("Human investors: " + App.getNumberOfHumanInvestors());
    }
    public void createInvestmentFund(){
        App.addInvestmentFund();
        investmentFundLabel.setText("Investment funds: " + App.getNumberOfInvestmentFunds());
    }

    public void switchToMainMenu(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(App.class.getResource("MainWindow.fxml"));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        currencyLabel.setText("Currencies: " + App.getNumberOfCurrencies());
        commodityLabel.setText("Commodities: " + App.getNumberOfCommodities());
        marketLabel.setText("Markets: " + App.getNumberOfMarkets());
        indexLabel.setText("Indices: " + App.getNumberOfIndices());
        companyLabel.setText("Companies: " + App.getNumberOfCompanies());
        humanInvestorLabel.setText("Human investors: " + App.getNumberOfHumanInvestors());
        investmentFundLabel.setText("Investments funds: " + App.getNumberOfInvestmentFunds());
    }
}
