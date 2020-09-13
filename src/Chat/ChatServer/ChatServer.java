package Chat.ChatServer;

import Chat.Messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is a simple server class it brokers clients connections  and then forwards messages to them
 */
public class ChatServer implements Runnable {
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String,ClientConnection> clients;
    private LinkedBlockingQueue<Message> serverMessageQ;
    private String serverHostname;
    private int serverport;
    private ChatServerListener chatServerListener;
    private boolean connected=false;

    public ChatServer(String serverHostname, int serverport,LinkedBlockingQueue<Message> serverMessageQ,ConcurrentHashMap<String,ClientConnection> clients){
        //TODO initialize everything. start the chatserver listener, and then start the chatserver thread.
        this. serverHostname=serverHostname;
        this.serverport = serverport;
        this.serverMessageQ=serverMessageQ;
        this.clients=clients;
        try {
            this.serverSocket=new ServerSocket(serverport);
            connected=true;
        } catch (IOException e) {
            System.out.println("Server can't listen on the specified port");
        }
        this.chatServerListener= new ChatServerListener(serverSocket,clients);

    }

    @Override
    public void run() {
        //TODO loop and wait on the serverMessageQ. respond to the messages accordingly.
        // update and maintain the clients map when applicable.
        System.out.println("Server is running");
        while (connected)
        {
            try {
                Message msg = null;
                msg = serverMessageQ.take();
                if (msg instanceof Message)
                {   //ClientConnection newClient =ClientConnection(serverSocket,serverMessageQ,clients);
                    //newClient.getUsername()
                }


        }
        catch (Exception e)
        { System.err.println(e);
        }

        }

    }

    //TODO build a shutdown method to gracefull close ALL client connections
    // and then shut down the listener and then shutdown its own thread
    public void shutdown()
    {
        //TODO close all sockets and shutdown the thread gracefully
        System.out.println("Connection to chat server is shut down");
        try {

        }
        finally {
            chatServerListener.shutdown();
            try {
                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            connected=false;
        }


    }
    public static void main(String[] args){

    }
}
