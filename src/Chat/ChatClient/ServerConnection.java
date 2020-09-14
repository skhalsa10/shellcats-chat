package Chat.ChatClient;

import Chat.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * this class encapsulates teh socket communication that the client has with the server.
 *
 * @author put name here
 * @author Siri wrote the skeleton
 *
 * @version 1.0
 */
public class ServerConnection implements Runnable{

    private Socket socket;
    //the input stream is all data coming to the client from the server
    private ObjectInputStream in;
    //the output stream is all the data going to the server
    private ObjectOutputStream out;
    private PriorityBlockingQueue<Message> clientMessageQ;
    private boolean connected = false;

    /**
     * Constructs the ServerConnection
     * @param socket socket used for connection
     * @param clientMessageQ client message queue
     * @throws IOException
     */
    public ServerConnection(Socket socket, PriorityBlockingQueue<Message> clientMessageQ) throws IOException {
        this.socket = socket;
        this.clientMessageQ = clientMessageQ;
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        connected = true;
    }
    /**
     * this function is what runs runnable thread is started.
     * This function will wait for messages to
     * come into the ObjectInputStream
     */
    @Override
    public void run() {

        //TODO read from the input stream and cast to message and put in the clientMessageQ
        // Make sure this is a Message type  before casting and output error if not Message type

        //TODO catch all errors that get thrown and handle appropriately.
        Message receivedMessage;
        while(connected) {
            try {
                Object receivedObject = in.readObject();
                if (receivedObject instanceof Message) {
                    receivedMessage = (Message) receivedObject;
                    clientMessageQ.put(receivedMessage);
                }
                else {
                    //TODO make this into an error...forgot how to do that...
                    System.out.println("not a Message!!!");
                }

            }
            catch(IOException e) {
                System.out.println("Chat Server connection is closed");
            }
            catch(Exception e) {
                System.err.println(e);
            }
        }
    }

    /**
     * This method will send a message down the output stream
     * @param m message to send out the socket
     */
    public void sendMessage(Message m){
        try {
            out.writeObject(m);
            System.out.println("message sent through serverConnection");
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Checks if server connection exists
     * @return true or false
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Close the output stream, input stream and socket.
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        try {
            out.close();
        }
        finally {
            try {
                in.close();
            }
            finally {
                socket.close();
            }
        }
    }

    /**
     * this method will gracefully shutdown everything
     */
    public void shutdown(){
        //TODO gracefully close input stream, output stream, and socket. then break out of the running loop.
        connected = false;
        try {
            closeConnection();
        }
        catch(IOException e) {
            System.out.println("Connection to Chat Sever closed");
        }
    }
}
