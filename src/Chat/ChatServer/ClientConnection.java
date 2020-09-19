package Chat.ChatServer;

import Chat.Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;
import java.io.IOException;

/**
 * This encapsulates a client connection on the server
 *
 * @author Soheila Jafari khouzani
 * @author siri wrote the skeleton
 *
 * @version 1
 */
public class ClientConnection implements Runnable{
    private String username;

    private Socket socket;
    //the input stream is all data coming to the client from the server
    private ObjectInputStream in;
    //the output stream is all the data going to the client
    private ObjectOutputStream out;
    //the server messageq
    private PriorityBlockingQueue<Message> serverMessageQ;
   // private ConcurrentHashMap<String,ClientConnection> clients;
    private boolean serverConnected=false;


    public ClientConnection(String username,Socket socket, PriorityBlockingQueue<Message> serverMessageQ)throws IOException {
        this.socket=socket;
        this.serverMessageQ=serverMessageQ;
        //this.clients=clients;
        this.username=username;
        out = new ObjectOutputStream(socket.getOutputStream()); // Server to client
        in = new ObjectInputStream(socket.getInputStream()); // From client to server
        serverConnected=true;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        System.out.println("clientconnection " + username + " running");
        Message receivedMessage;
        while(serverConnected) {
            try {
                Object receivedObject = in.readObject();
                if(receivedObject instanceof Message) {
                    receivedMessage = (Message) receivedObject;
                    if (receivedMessage instanceof ClientUserName) {
                        ((ClientUserName) receivedMessage).setTempUserName(this.username);
                        this.setUsername(((ClientUserName) receivedMessage).getUserName());
                        System.out.println(((ClientUserName) receivedMessage).getUserName());
                        serverMessageQ.put((receivedMessage));
                    }
                    else if(receivedMessage instanceof MShutDown){
                        try{
                            out.close();
                            in.close();
                            socket.close();
                        }
                        finally{
                            //force the thread to stop running
                            serverConnected = false;
                            //place message into  chat server to remove from list of clients.
                            serverMessageQ.put((receivedMessage));
                        }

                    }else if(receivedMessage instanceof MChat){
                        serverMessageQ.put(receivedMessage);
                    }
                    else{
                        System.out.println("Client connection cant process " + receivedMessage);
                    }

                }
                else {
                    System.out.println("It's not a valid message");
                }
            }
            catch(IOException e) {
                System.out.println("woeh woeh woeh there hold your horses and check client connection run");
                System.err.println(e);
                serverConnected = false;
            }
            catch(Exception e) {

                System.err.println(e);
                serverConnected = false;
            }

        }
    }

    /**
     * public interface to send a message to this client
     * @param m Message to send
     */
    public void sendMessage(Message m){

        try {
            out.writeObject(m);
        }
        catch(Exception e) {

            System.out.println("error sending message out " + username);
            serverMessageQ.put(new MFailedMessage(m,username));
            System.err.println(e);
        }

    }

    public void shutdown() throws IOException {
        //todo the server may initiate a shutdown and
        //System.out.println("Connection to chat server is shut down");
        try {
            out.close();
            in.close();
            socket.close();
        }
        finally {
            serverConnected=false;
        }

    }
}
