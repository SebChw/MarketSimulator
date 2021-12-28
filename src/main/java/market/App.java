package market;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
//! Tink about making the market about heroes 3!
import market.world.ControlPanel;
import market.world.World;
import market.gui.MarketPanelController;
import market.gui.asset.AssetViewController;
import market.gui.TradersPanelController;


public class App extends Application{
    private final static World world = new World();

    public static void main(String[] args) throws Exception {
        ControlPanel.run();

        
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        // world.addNewCurrency();
        
        // world.addNewCommodity();
        // world.addNewCommodity();
        // world.addNewCompany();
        world.addNewHumanInvestor();
        world.addNewInvestmentFund();
        // world.addNewCommodityMarket();
        // world.addNewCurrencyMarket();
        // world.addNewStockMarket();

        System.out.println("JEJ");
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("mainWindow.fxml"));
        
        Scene scene = new Scene(root);

        Image icon = new Image(App.class.getResourceAsStream("coin.png")); //this url is taken from javaFX docs
        stage.getIcons().add(icon);
        stage.setTitle("Market Simulator");

        stage.setScene(scene); //adding scene to the stage -> running everything
        stage.show();
    }

    public static void passAssetsToController(AssetViewController controller){
        controller.addAssets(world.getAllAssets());
    }

    public static void passMarketsToController(MarketPanelController controller){
        controller.addMarkets(world.getAllMarkets());
    }

    public static void passTradersToController(TradersPanelController controller){
        controller.addTraders(world.getAllTraders());
        
    }

    public static void addCurrency(){
        world.addNewCurrency();
    }

    public static void addCommodity(){
        world.addNewCommodity();
    }

    public static void addCompany(){
        world.addNewCompany();
    }

    public static void addMarket(){
        world.addNewRandomMarket();
    }
    public static void addHumanInvestor(){
        world.addNewHumanInvestor();
    }
    public static void addInvestmentFund(){
        world.addNewInvestmentFund();
    }
    public static int getNumberOfMarkets(){
        return world.getNumberOfMarkets();
    }
    public static int getNumberOfCurrencies(){
        return world.getNumberOfCurrencies();
    }
    public static int getNumberOfCommodities(){
        return world.getNumberOfCommodities();
    }
    public static int getNumberOfCompanies(){
        return world.getNumberOfCompanies();
    }
    public static int getNumberOfHumanInvestors(){
        return world.getNumberOfHumanInvestors();
    }
    public static int getNumberOfInvestmentFunds(){
        return world.getNumberOfInvestmentFunds();
    }
    public static int getNumberOfIndices(){
        return world.getNumberOfIndices();
    }
    
}
