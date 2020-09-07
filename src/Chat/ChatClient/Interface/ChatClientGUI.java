package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.Message;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * This will be the Javafx Graphical User Interface.
 * it will have a box that displays messages and it
 * will scroll with the oldest at the top and newest at the bottom.
 * there will be a text box to type into and a send button to send the message.
 *
 * @author put name here
 * @author siri created the skeleton
 *
 * @version 1.0
 */
public class ChatClientGUI extends AnimationTimer
{
    //will need to sendMessage to chat client when
    // we want to send an outgoing message to the server
    private ChatClient chatClient;
    private long lastUpdate = 0;
    private PriorityBlockingQueue<Message> interfaceMessageQ;
    private Stage stage;
    //TODO be prepared to declare MANY MANY objects here

    public ChatClientGUI(PriorityBlockingQueue<Message>interfaceMessageQ, Stage primaryStage, ChatClient chatClient){
        //TODO be prepared to have a giant constructor. this is typically unusual but for the gui you
        // are building everycomponant in the constructor and piecing it togethor. have fun :)
    }

    //when possible use a percentage for widths of the screensize you are working on.
    //you can changed this later after you make something that looks good
    //this will allow the screen to scale on all devices.

    @Override
    public void handle(long now) {
        //This will force any animations to be rendored at 60 frames per second
        //it may not actually be needed for this project
        // feel free to comment it out and test without it.
        if (now - lastUpdate >= 33_334_000) {

            //TODO process a single message in the message queue. it
            // is very important that you never stall this thread! so do not wait
            // on the InterfaceMessageQ. You should process 1 message if it exists and if
            // it doesn't than skip. InterfaceMessageQ.poll() will not block the thread.
            //The goal is to update the state of the interface based on the message
            // if it is an outgoing message also  forward the message to the chatclient to forward out the socket.
        // helped to stabalize the rendor time
            lastUpdate = now;
        }
    }
}
