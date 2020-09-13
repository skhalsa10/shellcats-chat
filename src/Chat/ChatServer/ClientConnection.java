package Chat.ChatServer;

import Chat.Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.io.IOException;
import java.io.EOFException;

/**
 * This encapsulates a client connection on the server
 *
 * @author Soheila Jafari khouzani
 * @author siri wrote the skeleton
 *
 * @version 1
 */
public class ClientConnection implements Runnable{
    private String username="Unkown";

    private Socket socket;
    //the input stream is all data coming to the client from the server
    private ObjectInputStream in;
    //the output stream is all the data going to the client
    private ObjectOutputStream out;
    //the server messageq
    private PriorityBlockingQueue<Message> serverMessageQ;
   // private ConcurrentHashMap<String,ClientConnection> clients;
    private boolean serverConnected=false;


    public ClientConnection(Socket socket, PriorityBlockingQueue<Message> serverMessageQ)throws IOException {
        //TODO initialize everything. the username will not be immediately known so set it to null or something.
        this.socket=socket;
        this.serverMessageQ=serverMessageQ;
        //this.clients=clients;
        //this.username=username;
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
        //TODO loop and wait on input stream  and place it into the serverMessageQ
        Message receivedMessage;
        try {
            Object receivedObject = in.readObject();
            while(receivedObject != null && serverConnected) {
                if (receivedObject instanceof Message) {
                    //Add message to server's serverMessageQ
                    receivedMessage = (Message) receivedObject;
                    if (receivedMessage instanceof ClientUserName) {
                     this.setUsername(((ClientUserName) receivedMessage).getUserName());
                    }
                    serverMessageQ.put((receivedMessage));

                }
                else
                { System.out.println("It's not a valid message");
                }
            }
            }
            catch(IOException  e)
            {
            System.out.println("A client disconnected and the socket closed.");
            } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }


    }

    public void sendMessage(Message m){
        //TODO send this message out the out pipe
        try {
            out.writeObject(m);
        }
        catch(Exception e) {
            System.err.println(e);
        }

    }

    public void shutdown() throws IOException {
        //TODO send a shutdown message down the out pipe and gracefully close
        // all streams, the socket, and break out of the thread loop
        out.writeObject("Connection to chat server is shut down");
        //System.out.println("Connection to chat server is shut down");
        try {
            out.close();
        }
        finally {
            try {
                in.close();
            }
            finally {
                try{
                    socket.close();
                }
                finally {
                    serverConnected=false;
                }


            }
        }



    }
}