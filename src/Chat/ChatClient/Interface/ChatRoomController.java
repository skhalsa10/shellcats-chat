package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;

public class ChatRoomController {

    private String clientUsername;
    private String recipientUsername;
    private ChatClient client;
    private PriorityBlockingQueue<Message> interfaceMessageQ;
    @FXML public TextField clientMessage;
    @FXML public TextArea messageLog;
    @FXML public TextField receiverUsername;
    @FXML public TextArea thisUsername;

    @FXML
    public void initialize() {
        new GUICommander();
    }

    /**
     * This method imports values from the previous GUI controller (the login GUI).
     * @param username
     * @param chatCLient
     * @param interfaceMessageQ
     */

    public void setData(String username, ChatClient chatCLient, PriorityBlockingQueue<Message> interfaceMessageQ) {
        this.clientUsername = username;
        this.client = chatCLient;
        this.interfaceMessageQ = interfaceMessageQ;
        thisUsername.appendText("Your username: " + clientUsername);
    }

    /**
     * This closes the GUI when you click the X at the top right corner.
     * @param event
     */

    @FXML
    public void handleClose(MouseEvent event) {
        interfaceMessageQ.put(new MShutDown(clientUsername));
        System.exit(0);
    }

    /**
     * This prints the user's message to the textarea of the chatroom GUI. It also adds the username and timestamp.
     */

    @FXML
    private void clickToSend() {
        String message = clientUsername + ": " + clientMessage.getText();
        this.recipientUsername = receiverUsername.getText();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        messageLog.appendText(timestamp + ": " + message + "\n");
        System.out.println("tomato" + message);
        MChat m = new MChat(clientUsername, recipientUsername, clientMessage.getText());
        client.sendMessage(m);
        clientMessage.clear();
    }

    /**
     * This creates another thread to listen for incoming messages and takes actions according to what it receives.
     */

    class GUICommander implements Runnable {
        private boolean GUICommanderRunning;

        public GUICommander() {

            new Thread(this).start();
           GUICommanderRunning = true;
        }

        @Override
        public void run() {
            while (GUICommanderRunning) {
                try {
                    Message incomingMessage = interfaceMessageQ.take();
                    if (incomingMessage instanceof MShutDown){
                        GUICommanderRunning = false;
                        client.sendMessage(incomingMessage);
                        if(System.console() == null) {
                            System.exit(0);
                        }
                    }
                    if (incomingMessage instanceof MUnavailable){
                        messageLog.appendText("The recipient client is unavailable.\n");
                    }
                    if (incomingMessage instanceof MChat){
                        String chatMessage = new String();
                        chatMessage = ((MChat) incomingMessage).getChatMessage();
                        System.out.println("potato" + chatMessage);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        messageLog.appendText(timestamp + ": " + (((MChat) incomingMessage).getSenderUsername()) + ": " + chatMessage + "\n");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

