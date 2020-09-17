package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.MChat;
import Chat.Messages.MSetRecipient;
import Chat.Messages.MShutDown;
import Chat.Messages.Message;

import java.util.Scanner;
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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    private PriorityBlockingQueue<Message> interfaceMessageQ;
    private ChatClient chatCLient;
    private String username;
    private boolean isRunning;
    private String recipient;


    /**
     * The constructor for the ChatCLientCLI
     * @param username the username of the client
     * @param serverHostName the hostname of the server to connect to
     * @param serverPort the port the server is listening on
     */
    public ChatClientCLI(String username, String serverHostName, int serverPort){
        this.username = username;
        interfaceMessageQ = new PriorityBlockingQueue<>();
        this.chatCLient = new ChatClient(username,serverHostName,serverPort,interfaceMessageQ);
        new Thread(this).start();
        isRunning = true;
        new CLICommander();
    }

    /**
     * this run will process messages that come in
     */
    @Override
    public void run() {
        while(isRunning){
            try {
                Message m = interfaceMessageQ.take();
                if(m instanceof MSetRecipient){
                    this.recipient = ((MSetRecipient) m).getRecipient();
                    chatCLient.sendMessage(m);
                }else if(m instanceof MChat){

                }
                else{
                    System.out.println("do not know how to process message inside of chatclientclie: " + m);
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * this is the main entry point for chatClient using CLI as the interface.
     * @param args - this should contain String username, String serverHostname, and int ServerPort
     */
    public static void main(String[] args){
        //todo add a research flag that helps automate some research

        if(args.length != 3){
            PrintInstructions();
            return;
        }
        String username = args[0];
        String serverHost = args[1];
        int serverPort = Integer.parseInt(args[2]);
        new ChatClientCLI(username,serverHost,serverPort);



    }

    class CLICommander implements Runnable{
        private boolean CLICommanderRunning;
        private Scanner scanner;


        public CLICommander(){
            scanner = new Scanner(System.in);

            new Thread(this).start();
            CLICommanderRunning = true;
        }

        @Override
        public void run() {
            while(CLICommanderRunning){
                String line = scanner.nextLine();
                //TODO here we need to check the line has any commands COMMAND:setRecipient...
                if(line.substring(0,7).equals("COMMAND")){
                    String[] parsedCommand = line.split(" ");
                    //process commands
                    if(parsedCommand[0].equals("COMMAND:setRecipient")){
                        interfaceMessageQ.put(new MSetRecipient(parsedCommand[1]));
                    }else if(parsedCommand[0].equals("COMMAND:logOff")){
                        interfaceMessageQ.put(new MShutDown(username));
                        scanner.close();
                        CLICommanderRunning = false;
                    }
                    else{
                        System.out.println("cant process " + line);
                    }
                }
                else{
                    MChat mchat = new MChat(username,recipient,line);
                }
                //if there is no command then create an MCHAT
            }

        }
    }

    private static void PrintInstructions() {
        System.out.println("Learn how to use this :)");
    }
}
