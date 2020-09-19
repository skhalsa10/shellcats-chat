package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.MChat;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatRoomController extends Application {

    private String clientUsername;
    private ChatClient client;

    public void setData (String username, ChatClient chatCLient){
        this.clientUsername = username;
        this.client = chatCLient;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("chatRoom.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1000, 572));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }
    @FXML
    private void clickToSend() {
        String message = clientUsername + ": " + clientMessage.getText();
        clientMessage.clear();
        messageLog.appendText(message + "\n");
        System.out.println(message);
        MChat m = new MChat(clientUsername, recipientUsername, clientMessage.getText());
        client.sendMessage(m);
    }
}
