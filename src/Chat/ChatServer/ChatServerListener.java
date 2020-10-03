package Chat.ChatServer;

import Chat.Messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * This class has one small purpose listen for new socket connections add them
 * to the clientConnection map/list so the server can use it later
 *
 * @author Soheila
 * @author Siri performed skeletons
 * @version 1.0
 */

public class ChatServerListener implements Runnable {
    private ServerSocket serverSocket;
    //the clients map will keep track of all clients but when a socket is created the username is not known
    //not sure if this will work but maybe keep a counter that gets incremented after every new connection the
    //username can be "notregistered1" the chat server will update the map when it learns the username.
    private ConcurrentHashMap<String,ClientConnection> clients;
    PriorityBlockingQueue<Message> serverMessageQ;
    private long counter;
    private boolean isRunning;


    /**
     * construct a new ChatServer Listener
     * @param serverSocket takes a server socket that class will listen on
     * @param clients a map containing all clients
     * @param serverMessageQ the server message queue to send messages to the sender
     */
    public ChatServerListener(ServerSocket serverSocket, ConcurrentHashMap<String,ClientConnection> clients, PriorityBlockingQueue<Message> serverMessageQ){
        this.clients=clients;
        this.serverSocket=serverSocket;
        this.serverMessageQ = serverMessageQ;
        counter=0;


    }

    /**
     * this method is the method that runs for the thread. the thread stays alive as long as run does not end.
     */
    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            try {

                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection("Null" + Long.toString(counter), clientSocket, serverMessageQ);
                clients.put("Null" + Long.toString(counter), clientConnection);
                counter++;
                Thread thread = new Thread(clientConnection);
                thread.start();
                RequestUsername usernameMsg = new RequestUsername();
                clientConnection.sendMessage(usernameMsg);

            } catch (Exception e) {
                System.out.println("catching all exceptions in the ServerListener class in the thread.");
                e.printStackTrace();
            }
        }
    }

    /**
     * gracefully close the serverListener
     */
    public void shutdown()
    {

        try {
            serverSocket.close();
            //break out of the running thread
            isRunning = false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection to chat server is shut down");

    }
}
