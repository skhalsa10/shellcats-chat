package Chat.ChatServer;

import Chat.Messages.ClientUserName;
import Chat.Messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
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

    public ChatServer(String serverHostname, int serverport){
        //TODO initialize everything. start the chatserver listener, and then start the chatserver thread.
        this.serverHostname=serverHostname;
        this.serverport = serverport;
        this.serverMessageQ=new LinkedBlockingQueue<>();
        this.clients=new ConcurrentHashMap<>();
        try {
            this.serverSocket=new ServerSocket(serverport);
            connected=true;
        } catch (IOException e) {
            System.out.println("Server can't listen on the specified port");
        }
        this.chatServerListener= new ChatServerListener(serverSocket,clients);
        Thread thread = new Thread(chatServerListener); //start server listener thread
        thread.start();
        Thread thread2 = new Thread(this); //start chat server thread
        thread2.start();
    }

    @Override
    public void run() {
        //TODO loop and wait on the serverMessageQ. respond to the messages accordingly.
        // update and maintain the clients map when applicable.
        System.out.println("Server is running");
        while (connected)
        {
            try {
                Message msg = serverMessageQ.take();
                if (msg instanceof ClientUserName)
                {
                    ClientUserName clientMsg=(ClientUserName)msg;
                    for (String key:clients.keySet())
                    {
                    if (clients.get(key).getUsername().equals(clientMsg.getUserName()))
                    {
                        ClientConnection clientConnection=clients.get(key); // Temporary store connection
                        clients.put(clientMsg.getUserName(),clientConnection);// Re added to map with correct key
                        clients.remove(key);  //Remove the old entry
                        break;
                    }
                    }
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
            for (ClientConnection clientConnection: clients.values()) {
               clientConnection.shutdown();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
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
        if (args.length != 2)
        {
            System.out.println("Wrong number of parameters");
        }
        ChatServer chatServer=new ChatServer(args[0], Integer.parseInt(args[1]));
    }
}
