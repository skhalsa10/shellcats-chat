package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * this is the main starting point for a ChatClient GUI
 */
public class GUIMain extends Application {
    private ChatClientGUI gui;
    private ChatClient chatClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO initialize the GUI and chatclient pass appropriate arguments make a temp variable to
        // store the InterfaceMessageQ this can be passed to the GUI and Chat client to connect the two.
        //you can grab the arguments using getParameters().getUnnamed().get(index of argument);
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        //TODO left example on how to force an appropriate check for arguments

        //if(args.length == 4) {
            launch(args);
        //}
        //else {
        //    System.out.println("Arguments: Bank Hostname, Bank Port, Agent Name, Initial Balance");
        //    System.exit(0);
        //}

    }
}
