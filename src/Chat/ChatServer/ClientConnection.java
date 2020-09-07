package Chat.ChatServer;

import Chat.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This encapsulates a client connection on the server
 *
 * @author put name here
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


    public ClientConnection(Socket clientSocket, PriorityBlockingQueue<Message> serverMessageQ){
        //TODO initialize everything. the username will not be immediately known so set it to null or something.
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
    }

    public void sendMessage(Message m){
        //TODO send this message out the outpipe
    }

    public void shutdown(){
        //TODO send a shutdown message down the out pipe and gracefully close
        // all streams, the socket, and break out of the thread loop
    }
}
