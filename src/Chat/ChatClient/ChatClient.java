package Chat.ChatClient;

import Chat.Messages.Message;

import java.util.concurrent.PriorityBlockingQueue;
import java.net.Socket;


/**
 * this is a very simple class it is essentialy a message router
 * it accepts messages and forwards them to an interface to display
 * and it will send the message out the socket if the message has a destination
 * that is not its own username
 *
 * @author Put name here
 * @author Siri wrote the skeleton
 *
 * @version 1.0
 */
public class ChatClient implements Runnable{

    private ServerConnection serverConnection;
    private PriorityBlockingQueue<Message> messageQ;
    private String username;
    private String serverHostName;
    private int serverPort;
    private boolean isRunning; //flag to represent running state.
    //this queue will send messages to the CLI or GUI and it
    // doesnt matter which class is running on the other end.
    private PriorityBlockingQueue<Message> interfaceMessageQ;

    /**
     * the constructor of the ChatClient. It will initialize everything and then loop
     * @param username client username
     * @param serverHostName server hostname
     * @param serverPort server port number
     * @param interfaceMessageQ message queue for client interface
     */
    public ChatClient(String username, String serverHostName, int serverPort, PriorityBlockingQueue<Message> interfaceMessageQ){
        //TODO initialize everything here. add any  more parameters as
        // needed. Build the server connection as well. finally start the thread.
        this.username = username;
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.interfaceMessageQ = interfaceMessageQ;
        connectToServer(this.serverHostName, this.serverPort);
        new Thread(this).start();
    }
    /**
     * this method gets run when the thread is started
     * it will need to loop for the life of the chatclient
     * and process messages on the messageQ
     */
    @Override
    public void run() {
        //TODO loop over the messageQ and process it. make sure to forward
        // incoming messages to the interfaceMessageQ if it needs to be
        // displayed. If it is an outgoing message send it out the serverConnection
    }

    /**
     * this is a public method that allows a message to be placed in the messageG of the ChatClient
     * @param M message to be placed in the queue
     */
    public void sendMessage(Message M){
        //TODO just drop this message into the messageQ and it will get processed when ready.
    }

    private void shutdown(){
        //TODO this gets called when a Shutdown message is received it will gracefully close all
        // connections and the break out of run loop
    }

    /**
     * Connect to the server with the specifed hostname and port number
     * @param serverHostName server host name
     * @param serverPort server port number
     */
    private void connectToServer(String serverHostName, int serverPort) {
        try {
            Socket socket = new Socket(serverHostName, serverPort);
            this.messageQ = new PriorityBlockingQueue<Message>();
            serverConnection = new ServerConnection(socket, messageQ);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
