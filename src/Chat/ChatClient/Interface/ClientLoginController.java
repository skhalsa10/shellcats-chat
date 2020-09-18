package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    @FXML public TextField clientMessage;
    @FXML public TextArea messageLog;

    public void loginButtonClicked() throws IOException {
        System.out.println("Clicked");
        try {
            this.stringPort = port.getText();
            this.interfaceMessageQ = new PriorityBlockingQueue<>();
            client = new ChatClient(username.getText(),serverIP.getText(),Integer.parseInt(stringPort),interfaceMessageQ);
            Stage stage;
            stage = (Stage) serverIP.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("chatRoom.fxml"));
            stage.setScene(new Scene(root, 1000, 572));
            stage.setResizable(false);
            stage.show();
        }
        catch (NumberFormatException e ) {
            System.out.println("Arguments: String username, int Server, int Port number");
            System.out.println("Please try again");
        }
    }
    @FXML
    private void handleClose(MouseEvent event)
    {
        System.exit(0);
    }
    @FXML
    private void clickToSend() {
        String message = username + ": " + clientMessage.getText();
        clientMessage.clear();
        messageLog.appendText(message + "\n");
        System.out.println(message);
    }
}