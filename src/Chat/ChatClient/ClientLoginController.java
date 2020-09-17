package Chat.ChatClient;

import Chat.Messages.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;

public class ClientLoginController {

    private ChatClient client;
    private PriorityBlockingQueue<Message> interfaceMessageQ;

    @FXML public TextField username;
    @FXML public TextField serverIP;
    @FXML public TextField port;
    @FXML public String stringPort;

    public void loginButtonClicked() throws IOException {
        System.out.println("Clicked");
        try {
            this.stringPort = port.getText();
            client = new ChatClient(username.getText(),serverIP.getText(),Integer.parseInt(stringPort),interfaceMessageQ);
        }
        catch (NumberFormatException e ) {
            System.out.println("Arguments: String username, int Server, int Port number");
            System.out.println("Please try again");
        }
    }

    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }
}