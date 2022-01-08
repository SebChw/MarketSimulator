package market;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import market.world.World;
import market.entityCreator.SemiRandomValuesGenerator;
import market.gui.MainPanelController;
import market.markets.Market;
import javafx.application.Platform;
import market.traders.Trader;
import javafx.concurrent.Task;

//!THE PROBLEM OF NEGATIVE AMOUNT IN CIRCULATION EMERGES AS SOMEHOW ASSET CAN HAVE NEGATIVE RATIO!

//Create one window with more tables!
public class App extends Application {
    //alternative -> when creating scene pass a World as a paramter
    public static boolean isRunning = true;
    public static void main(String[] args) throws Exception {
        System.out.println((int)(0.9f));
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        World world = new World("Spice");

        FXMLLoader loader =  new FXMLLoader(App.class.getResource("mainWindow.fxml"));
        
        MainPanelController controller = new MainPanelController(world);
        
        loader.setController(controller);

        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        Image icon = new Image(App.class.getResourceAsStream("coin.png")); //this url is taken from javaFX docs
        stage.getIcons().add(icon);
        stage.setTitle("Market Simulator");

        stage.setScene(scene); //adding scene to the stage -> running everything

        stage.show();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
               Runnable updater = new Runnable() {
   
                  @Override
                  public void run() {
                     controller.updateDate();
                  }
               };
   
               while (isRunning) {
                  try {
                     Thread.sleep(5000);
                  } catch (InterruptedException ex) {}
   
                  // UI update is run on the Application thread
                  world.updateAllRates();
                  if(!world.getInvestmentFunds().isEmpty()){
                     SemiRandomValuesGenerator.getRandomInvestmentFund(world.getInvestmentFunds()).IssueFundUnit();
                  }
                  Platform.runLater(updater);
               }
            }
         });
         // don't let thread prevent JVM shutdown
         thread.setDaemon(true);
         thread.start();
    }

    @Override
   public void stop(){
    System.out.println("Stage is closing");
    isRunning = false;
   }
}
