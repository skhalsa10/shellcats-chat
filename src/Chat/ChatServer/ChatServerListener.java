package Chat.ChatServer;

import Chat.Messages.*;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

// This is Soheila
/**
 * This class has one small purpose listen for new socket connections add them
 * to the clientConnection map/list so the server can use it later
 */

public class ChatServerListener implements Runnable {
    private ServerSocket serverSocket;
    //the clients map will keep track of all clients but when a socket is created the username is not known
    //not sure if this will work but maybe keep a counter that gets incremented after every new connection the
    //username can be "notregistered1" the chat server will update the map when it learns the username.
    private ConcurrentHashMap<String,ClientConnection> clients;
    PriorityBlockingQueue<Message> serverMessageQ;
    private long counter;


    public ChatServerListener(ServerSocket serverSocket, ConcurrentHashMap<String,ClientConnection> clients){
        //TODO initialize anything and start the thread.
        this.clients=clients;
        this.serverSocket=serverSocket;
        counter=0;

    }
    @Override
    public void run() {
        try {
            Socket clientSocket = serverSocket.accept();
            ClientConnection clientConnection = new ClientConnection(clientSocket, serverMessageQ);
            clients.put("Null"+Long.toString(counter),clientConnection);
            counter++;
            Thread thread = new Thread(clientConnection);
            thread.start();
            System.out.println("Client connected!");
            requestUsername usernameMsg=new requestUsername();
            clientConnection.sendMessage(usernameMsg);

        } catch (IOException e) {
            e.printStackTrace();

            //TODO wait for new connections on the socket and create a ClientConnection.
            // Add it to the clients map. and finally send the client a message requesting that
            // it send over its username info so the server can maintain the clients map more efficiently.
        }
    }

    public void shutdown()
    {
        //TODO close all sockets and shutdown the thread gracefully

        System.out.println("Connection to chat server is shut down");
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
