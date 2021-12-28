package market.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    private Stage stage;
    private Scene scene;
    private Parent root; //The base class for all nodes that have children in the scene graph
    
    public void switchScene(Parent root, ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAssetView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainMenuController.class.getResource("assetsScene.fxml"));
        switchScene(root, event);
    }

    public void switchToControlPanelView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainMenuController.class.getResource("controlPanelScene.fxml"));
        switchScene(root, event);  
    }

    public void switchToMarketView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainMenuController.class.getResource("marketsScene.fxml"));
        switchScene(root, event);  
    }

    public void switchToTraderView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainMenuController.class.getResource("tradersScene.fxml"));
        switchScene(root, event);  
    }

}
