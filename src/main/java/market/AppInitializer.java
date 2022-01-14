package market;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import market.world.World;
import market.world.WorldThread;
import market.gui.MainPanelController;

/**
 * Class used to Initialize entire Market application
 */
public class AppInitializer extends Application {

   /**
    * Global indicator for all threads to know when aplication is closed
    */
   public static boolean isRunning = true;

   /**
    * Just runs lunch method with given args
    * 
    * @param args
    * @throws Exception
    */
   public static void main(String[] args) {
      launch(args);
   }

   /**
    * Function which initializes world, load all necessary controllers and set
    * stage
    * 
    * @param stage
    * @throws IOException
    */
   @Override
   public void start(Stage stage) throws IOException {
      World world = new World("Spice");

      FXMLLoader loader = new FXMLLoader(AppInitializer.class.getResource("mainWindow.fxml"));

      MainPanelController controller = new MainPanelController(world);

      loader.setController(controller);

      Parent root = loader.load();

      Scene scene = new Scene(root);

      Image icon = new Image(AppInitializer.class.getResourceAsStream("coin.png")); // this url is taken from javaFX
                                                                                    // docs
      stage.getIcons().add(icon);

      stage.setScene(scene); // adding scene to the stage -> running everything

      stage.show();

      Thread thread = new WorldThread(world, controller);

      // don't let thread prevent JVM shutdown
      thread.setDaemon(true);
      thread.start();
   }

   @Override
   /**
    * This function sets AppInitializer.isRunning to false so that all threads stop
    */
   public void stop() {
      isRunning = false;
   }
}
