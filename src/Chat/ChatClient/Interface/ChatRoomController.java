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

    @FXML
    public void initialize() {
        new GUICommander();
    }

    public void setData(String username, ChatClient chatCLient, PriorityBlockingQueue<Message> interfaceMessageQ) {
        this.clientUsername = username;
        this.client = chatCLient;
        this.interfaceMessageQ = interfaceMessageQ;
    }

    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void clickToSend() {
        String message = clientUsername + ": " + clientMessage.getText();
        this.recipientUsername = receiverUsername.getText();
        clientMessage.clear();
        messageLog.appendText(message + "\n");
        System.out.println(message);
        MChat m = new MChat(clientUsername, recipientUsername, clientMessage.getText());
        client.sendMessage(m);
    }

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
                    }
                    if (incomingMessage instanceof MUnavailable){
                        //TODO say it ain't availble
                    }
                    if (incomingMessage instanceof MChat){
                        //TODO appendtext to textarea of chatroom
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

