package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * this class is the controller for the chat room gui
 */
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
        // Checks for an empty string
        if (clientMessage.getText().equals("")){
            return;
        }
        // else it will send the message
        String message = clientUsername + ": " + clientMessage.getText();
        this.recipientUsername = receiverUsername.getText();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        messageLog.appendText(timestamp + ": " + message + "\n");
        MChat m = new MChat(clientUsername, recipientUsername, clientMessage.getText());
        client.sendMessage(m);
        clientMessage.clear();
    }

    /**
     * this allows user to press enter to also send message rather than just pressing the send button.
     * @param keyEvent
     */
    @FXML
    public void handleEnterKey(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            clickToSend();
        }
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
            clientMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        clickToSend();
                    }
                }
            });
            while (GUICommanderRunning) {
                try {
                    Message incomingMessage = interfaceMessageQ.take();
                    if (incomingMessage instanceof MShutDown){
                        GUICommanderRunning = false;
                        client.sendMessage(incomingMessage);
                        System.exit(0);
                    }
                    if (incomingMessage instanceof MUnavailable){
                        messageLog.appendText("The recipient client is unavailable.\n");
                    }
                    if (incomingMessage instanceof MChat){
                        String chatMessage = new String();
                        chatMessage = ((MChat) incomingMessage).getChatMessage();
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

