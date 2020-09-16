package Chat.ChatServer;

import Chat.Messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class is a simple server class it brokers clients connections  and then forwards messages to them
 */
public class ChatServer implements Runnable {
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String,ClientConnection> clients;
    private PriorityBlockingQueue<Message> serverMessageQ;
    private String serverHostname;
    private int serverport;
    private ChatServerListener chatServerListener;
    private boolean connected=false;

    public ChatServer(String serverHostname, int serverport){
        //TODO initialize everything. start the chatserver listener, and then start the chatserver thread.
        this.serverHostname=serverHostname;
        this.serverport = serverport;
        this.serverMessageQ = new PriorityBlockingQueue<>();
        this.clients=new ConcurrentHashMap<>();
        try {
            this.serverSocket=new ServerSocket(serverport);
            connected=true;
        } catch (IOException e) {
            System.out.println("Server can't listen on the specified port");
        }
        this.chatServerListener= new ChatServerListener(serverSocket,clients, serverMessageQ);
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
                System.out.println(msg);
                if (msg instanceof ClientUserName)
                {
                    ClientUserName clientMsg=(ClientUserName)msg;
                    for (String key:clients.keySet())
                    {
                        System.out.println("THE KEY is " + key);
                        if (clients.get(key).getUsername().equals(clientMsg.getUserName()))
                        {
                            ClientConnection clientConnection=clients.get(key); // Temporary store connection
                            clients.put(clientMsg.getUserName(),clientConnection);// Re added to map with correct key
                            clients.remove(key);  //Remove the old entry
                            break;
                        }
                    }
                    MShutDown m = new MShutDown(clientMsg.getUserName());
                    ClientConnection cc = clients.get(clientMsg.getUserName());
                    cc.sendMessage(m);
                }
                else if (msg instanceof MChat) {
                    String recipient = ((MChat) msg).getRecipientUsername();
                    if (clients.containsKey(recipient)) {
                        ClientConnection clientConnection = clients.get(recipient);
                        clientConnection.sendMessage(msg);
                    }
                    else {
                        String sender = ((MChat) msg).getSenderUsername();
                        MUnavailable m = new MUnavailable(sender, recipient);
                        ClientConnection clientConnection = clients.get(sender);
                        clientConnection.sendMessage(m);
                    }
                }
                else if (msg instanceof MShutDown) {
                    String sender = ((MShutDown) msg).getUsername();
                    ClientConnection clientConnection = clients.get(sender);
                    try {
                        clientConnection.shutdown();
                    }
                    catch(IOException e) {
                        System.out.println("Connection to " + sender + "is closed");
                    }
                }



            }
            catch (Exception e)
            {
                System.out.println("catching error in processing messages in ChatServer Run");
                System.err.println(e);
            }

        }

    }

    //TODO build a shutdown method to gracefull close ALL client connections
    // and then shut down the listener and then shutdown its own thread
    public void shutdown()
    {
        //TODO close all sockets and shutdown the thread gracefully
        System.out.println("Processing shutdown ");
        try {
            System.out.println("shutting down remaining clients");
            for (ClientConnection clientConnection: clients.values()) {
                clientConnection.shutdown();
            }
        }
        catch (IOException e) {
            System.out.println("cought error in chatserver shutdown");
            e.printStackTrace();
        }
        finally {
            System.out.println("shutting down the chat server listener");
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
