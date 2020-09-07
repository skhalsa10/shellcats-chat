package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.Message;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * the purpose of this class is to act as a command line interface for the chat client.
 * it will represent the chat conversation and will allow the user
 * to write something into the console and press enter to send it.
 *
 * May be able to build in an automated research test using this.
 *
 * @author place name here
 * @author siri did the skeleton
 */
public class ChatClientCLI implements Runnable{
    private PriorityBlockingQueue<Message> interfaceMessageQ;
    private ChatClient chatCLient;


    @Override
    public void run() {
        //TODO process messages in queue
        //write it to standard out and
        //forward to ChatClient if needed
    }

    /**
     * this is the main entry point for chatClient using CLI as the interface.
     * @param args - this should contain String username, String serverHostname, and int ServerPort
     */
    public static void main(String[] args){

        //TODO initialize everything.

        //TODO loop waiting on standard in. place standard in string into a
        // message and drop into interfaceMessageQ to process later by run above.
        // This should work... but not confident...
    }
}
