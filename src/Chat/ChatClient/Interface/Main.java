package Chat.ChatClient.Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class creates a GUI by creating a stage a filling it with data loaded from clientlogin.fxml
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("clientLogin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 572));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//javafx.stage.Screen.getPrimary().getBounds().getMaxX();
//javafx.stage.Screen.getPrimary().getBounds().getMaxY();
//0.625*javafx.stage.Screen.getPrimary().getBounds().getMaxX();