package Chat.ChatClient.Interface;

import Chat.ChatClient.ChatClient;
import Chat.Messages.*;

import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * the purpose of this class is to act as a command line interface for the chat client.
 * it will represent the chat conversation and will allow the user
 * to write something into the console and press enter to send it.
 *
 * May be able to build in an automated research test using this.
 *
 * @author Siri Khalsa
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
    private CLICommander commander;
    private boolean researchMode = false;

    /**
     * The constructor for the ChatCLientCLI
     * @param username the username of the client
     * @param serverHostName the hostname of the server to connect to
     * @param serverPort the port the server is listening on
     */
    public ChatClientCLI(String username, String serverHostName, int serverPort, boolean researchMode, String recipient, int researchMessages){
        this.username = username;
        interfaceMessageQ = new PriorityBlockingQueue<>();
        this.researchMode = researchMode;
        this.recipient = recipient;
        interfaceMessageQ.put(new MSetRecipient(this.recipient));
        this.chatCLient = new ChatClient(username,serverHostName,serverPort,interfaceMessageQ, recipient,this.researchMode,researchMessages);
        new Thread(this).start();
        isRunning = true;
        if(!researchMode) {
            this.commander = new CLICommander();
        }
        else {
            System.out.println("the commander did not start");
        }
    }

    /**
     * The constructor for the ChatCLientCLI
     * @param username the username of the client
     * @param serverHostName the hostname of the server to connect to
     * @param serverPort the port the server is listening on
     */
    public ChatClientCLI(String username, String serverHostName, int serverPort){
        this.username = username;
        interfaceMessageQ = new PriorityBlockingQueue<>();
        interfaceMessageQ.put(new MSetRecipient(this.recipient));
        this.chatCLient = new ChatClient(username,serverHostName,serverPort,interfaceMessageQ);
        new Thread(this).start();
        isRunning = true;
        if(!researchMode) {
            this.commander = new CLICommander();
        }
        else {
            System.out.println("the commander did not start");
        }
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
                    System.out.println("is the chatclient null? " + chatCLient);
                    chatCLient.sendMessage(m);
                    System.out.println(ANSI_GREEN + "PROCESSED COMMAND:setRecipient " + ANSI_RESET);
                }else if(m instanceof MChat){
                    MChat mChat = (MChat) m;
                    if(mChat.getSenderUsername() == username){
                        //print out messages being sent by this client
                        System.out.println(ANSI_CYAN + mChat.getChatMessage() + ANSI_RESET);
                        chatCLient.sendMessage(mChat);
                    }else{
                        //print out messages received
                        System.out.println(ANSI_BLACK + ANSI_WHITE_BACKGROUND + mChat.getChatMessage() + ANSI_RESET);
                    }
                }
                else if(m instanceof MShutDown){
                    MShutDown m2 = (MShutDown)m;
                    System.out.println(ANSI_RED + "PROCESSED COMMAND:logOff " + ANSI_RESET);
                    isRunning = false;
                    chatCLient.sendMessage(m2);
                }
                else if(m instanceof MUnavailable){
                    System.out.println(ANSI_RED + "Recipient " + ((MUnavailable) m).getRecipient() + " is not available."+ ANSI_RESET);
                }
                else if(m instanceof MStopResearch) {
                    //System.out.println("stop the research!!!!");
                    //System.out.println("i am the user " + username);
                    interfaceMessageQ.put(new MShutDown(username));
                }
                else{
                    System.out.println("do not know how to process message inside of chatclientclie: " + m);
                }



            } catch (InterruptedException e) {
                System.out.println("error in chatclientCLI run to process messages");
                e.printStackTrace();
            }
        }
        System.out.println("CLI is shutting down");

    }

    /**
     * this is the main entry point for chatClient using CLI as the interface.
     * @param args - this should contain String username, String serverHostname, and int ServerPort
     */
    public static void main(String[] args){

        if(args.length != 3 && args.length != 6){
            PrintInstructions();
            return;
        }
        String username = args[0];
        String serverHost = args[1];
        int serverPort = Integer.parseInt(args[2]);

        if (args.length == 6 && args[3].equalsIgnoreCase("research")) {

            ChatClientCLI clientCLI = new ChatClientCLI(username,serverHost,serverPort, true, args[4], Integer.parseInt(args[5]));
            //clientCLI.chatCLient.setResearchMode();
            //clientCLI.recipient = args[4];
            //clientCLI.chatCLient.setResearchMessages(Integer.parseInt(args[5]));
            //clientCLI.interfaceMessageQ.put(new MSetRecipient(clientCLI.recipient));
            //System.out.println(clientCLI.researchMode);
            //System.out.println(clientCLI.recipient);

        }
        else {
            ChatClientCLI clientCLI = new ChatClientCLI(username,serverHost,serverPort);
        }



    }

    /**
     * this is a nested class that listens for commends on standard in
     * this is not needed in research mode and is only started with human input
     */
    class CLICommander implements Runnable{
        private boolean CLICommanderRunning;
        private Scanner scanner;


        public CLICommander(){
            scanner = new Scanner(System.in);

            new Thread(this).start();
            CLICommanderRunning = true;
        }

        /**
         * this thread will take in a line from standard in. if it is a command it
         * processes the command if it is nto a command it will send a message with the text.
         */
        @Override
        public void run() {
            while(CLICommanderRunning){
                String line = scanner.nextLine();
                if((line.length()>7)&&line.substring(0,7).equals("COMMAND")){
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
                    interfaceMessageQ.put(mchat);
                }
                //if there is no command then create an MCHAT
            }

            System.out.println("Leaving the Commander!");
        }

    }

    private static void PrintInstructions() {
        System.out.println("Learn how to use this :)");
    }

}
