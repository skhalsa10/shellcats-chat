package Chat.ChatClient;

import Chat.Messages.*;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.Duration;



/**
 * this is a very simple class it is essentialy a message router
 * it accepts messages and forwards them to an interface to display
 * and it will send the message out the socket if the message has a destination
 * that is not its own username
 *
 * @author Michelle Louie
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

    private String recipient;
    private boolean researchMode = false;
    private int numChatMsgs = 0;
    private ArrayList<String> delayTimes = new ArrayList<>() ;


    /**
     * the constructor of the ChatClient. It will initialize everything and then loop
     * @param username client username
     * @param serverHostName server hostname
     * @param serverPort server port number
     * @param interfaceMessageQ message queue for client interface
     */
    public ChatClient(String username, String serverHostName, int serverPort, PriorityBlockingQueue<Message> interfaceMessageQ, String recipient, boolean researchMode , int researchMessages){

        this.username = username;
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.interfaceMessageQ = interfaceMessageQ;
        this.recipient = recipient;
        this.researchMode = researchMode;
        this.numChatMsgs = researchMessages;
        connectToServer(this.serverHostName, this.serverPort);
        new Thread(this).start();
    }

    /**
     * the constructor of the ChatClient. It will initialize everything and then loop
     * @param username client username
     * @param serverHostName server hostname
     * @param serverPort server port number
     * @param interfaceMessageQ message queue for client interface
     */
    public ChatClient(String username, String serverHostName, int serverPort, PriorityBlockingQueue<Message> interfaceMessageQ){

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
        if(serverConnection == null) {
            interfaceMessageQ.put(new MShutDown(username));
        }
        else if (serverConnection.isConnected()) {
            isRunning = true;
            while(isRunning) {
                takeMessage();
            }
        }
        System.out.println("ChatClient is shutting down");
    }

    /**
     * Takes messages from messageQ and processes them
     */
    private void takeMessage() {
        Message receivedMessage;
        try {
            receivedMessage = messageQ.take();
            processMessage(receivedMessage);
        }
        catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Processes message by its type
     * Send message to the correct message queue
     * @param m message
     */
    private void processMessage(Message m) {
        if(m instanceof MShutDown) {
            System.out.println("ChatCLient Received MShutDown");
            shutdown((MShutDown) m);
        }
        else if(m instanceof MChat) {
            forwardMessage((MChat) m);
        }
        else if (m instanceof RequestUsername) {
            sendUsername();
        }
        else if (m instanceof MUnavailable) {
            String recipient = ((MUnavailable) m).getRecipient();
            System.out.println(recipient + " is unavailable");
            interfaceMessageQ.put(m);
        }
        else if(m instanceof MUsernameExists) {
            System.out.println(((MUsernameExists) m).getMsg());
            interfaceMessageQ.put(m);
            this.username = ((MUsernameExists) m).getTempName();
            interfaceMessageQ.put(new MShutDown(username));
        }
        else if (m instanceof MSetRecipient) {
            this.recipient = ((MSetRecipient) m).getRecipient();
        }
        else if (m instanceof MSpam) {
            for(int i = 0; i < numChatMsgs; i++) {
                MChat msg = new MChat(username, recipient, "Spam? To make spam musubi???");
                interfaceMessageQ.put(msg);
            }
        }
        else if (m instanceof MStopResearch) {
            System.out.println("I the client must stop the research");
            interfaceMessageQ.put(m);
        }
    }

    /**
     * Send username to server
     */
    private void sendUsername() {
        ClientUserName m = new ClientUserName(username);
        if(serverConnection != null) {
            serverConnection.sendMessage(m);
        }
    }

    /**
     * Forward message to either interface or server
     * @param m message to be sent
     */
    private void forwardMessage(MChat m) {
        String recipient = m.getRecipientUsername();
        System.out.println("the RECIPIENT from the MChat message  IS " + recipient);
        if(recipient.equalsIgnoreCase(username)) {
            // in research mode record the time it took for the message to arrive
            if(researchMode) {
                //Duration duration = Duration.between(LocalDateTime.now(), m.getTimeStamp());
                Duration duration = Duration.between(m.getTimeStamp(), LocalDateTime.now());
                System.out.println("msg time: " + m.getTimeStamp());
                System.out.println("local time: " + LocalDateTime.now());
                long secondsDelay = Math.abs(duration.getSeconds());
                long delay = Math.abs(duration.getNano());
                delayTimes.add(String.valueOf(secondsDelay + (delay/(0.1e10))));
                if(delayTimes.size() == numChatMsgs) {
//                    double total = 0;
//                    for(Double i : delayTimes) {
//                        total += i;
//                    }
//                    double avgDelay = total/numChatMsgs;
                    MDelayTimes msg = new MDelayTimes(m.getSenderUsername(), m.getRecipientUsername(),
                                        delayTimes, "0", numChatMsgs);
                    if(serverConnection != null) {
                        serverConnection.sendMessage(msg);
                    }
                }
            }
            interfaceMessageQ.put(m);
        }
        else {
            if(serverConnection != null) {
                serverConnection.sendMessage(m);
            }
        }
    }

    /**
     * this is a public method that allows a message to be placed in the messageG of the ChatClient
     * @param m message to be placed in the queue
     */
    public void sendMessage(Message m){
        if(messageQ != null) {
            messageQ.put(m);
        }
    }

    /**
     * ShutDown message received
     * Notify server that client is shutting down and shut down client and close all connections
     * @param m shutdown message
     */
    private void shutdown(MShutDown m){
        isRunning = false;
        if(serverConnection != null) {
            serverConnection.sendMessage(m);
            serverConnection.shutdown();
        }


    }


    /**
     * Connect to the server with the specified hostname and port number
     * @param serverHostName server host name
     * @param serverPort server port number
     */
    private void connectToServer(String serverHostName, int serverPort) {
        try {
            Socket socket = new Socket(serverHostName, serverPort);
            this.messageQ = new PriorityBlockingQueue<Message>();
            System.out.println("socket is connected?" + socket.isConnected());
            serverConnection = new ServerConnection(socket, messageQ);
            new Thread(serverConnection).start();
        }
        catch (Exception e) {
//            e.printStackTrace();
            System.out.println("error connecting to server");
//            System.err.println(e);
        }
    }

}
