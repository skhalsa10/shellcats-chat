package Chat.ChatServer;

import Chat.Messages.Message;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
// This is Soheila
/**
 * This class has one small purpose listen for new socket connections add them
 * the the clientConnection map/list so the server can use it later
 */
public class ChatServerListener implements Runnable {
    private Socket serverSocket;
    //the clients map will keep track of all clients but when a socket is created the username is not known
    //not sure if this will work but maybe keep a counter that gets incremented after every new connection the
    //username can be "notregistered1" the chat server will update the map when it learns the username.
    private ConcurrentHashMap<String,ClientConnection> clients;
    LinkedBlockingQueue<Message> serverMessageQ;
    private long counter;

    public ChatServerListener(Socket serverSocket, ConcurrentHashMap<String,ClientConnection> clients){
        //TODO initialize anything and start the thread.
    }
    @Override
    public void run() {
        //TODO wait for new connections on the socket and create a ClientConnection.
        // Add it to the clients map. and finally send the client a message requesting that
        // it send over its username info so the server can maintain the clients map more efficiently.
    }

    public void shutdown(){
        //TODO close all sockets and shutdown the thread gracefully
    }
}
