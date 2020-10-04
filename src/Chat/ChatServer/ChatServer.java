package Chat.ChatServer;

import Chat.Messages.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
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

    private boolean researchMode = false;
    private int totalNumClients = 0;
    private int countClients = 0;
    private int totalNumDelayMsgs = 0;
    private String logFileName = "logC2C.csv";
    private String logFileNameServer = "logC2S.csv";
    private int totalMChatMsgs = 0;

    public ChatServer(String serverHostname, int serverport){
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

    /**
     * this is the run function for the thread of the server that processes messages
     */
    @Override
    public void run() {

        System.out.println("Server is running");
        while (connected)
        {
            try {
                Message msg = serverMessageQ.take();
                if (msg instanceof ClientUserName)
                {
                    ClientUserName clientMsg = (ClientUserName) msg;
                    //if the username exists
                    if(clients.containsKey(clientMsg.getUserName())) {
                       String clientName = clientMsg.getTempUserName();
                       ClientConnection clientConnection = clients.get(clientName);
                       MUsernameExists m = new MUsernameExists(clientMsg.getUserName(), clientName);
                       clientConnection.sendMessage(m);
                    }
                    //if the username does not yet exist
                    else {
                        //remove clientconenction with wrong key and store it temporarily
                        ClientConnection clientConnection = clients.remove(clientMsg.getTempUserName());
                        if (clientConnection == null) {
                            System.out.println("ERROR in chatserver");
                        }
                        //place it into the clients with correct key
                        clients.put(clientMsg.getUserName(), clientConnection);
                        countClients++;
                        if(researchMode) {
                            System.out.println("Client Count is at " +countClients);
                        }

                        // For research mode: check if total number of clients needed is reached

                        if(researchMode && countClients == totalNumClients) {
                            int numSendingClients = totalNumClients/2;
                            for(int i = 1; i <= numSendingClients; i++) {
                                String sendingClient = "client" + Integer.toString(i);
                                ClientConnection cc = clients.get(sendingClient);
                                System.out.println("can I send you a message? this client is: " + cc);
                                if(cc == null){
                                    System.out.println("did this client name never get updated from null? " + Integer.toString(i-1) );
                                    System.out.println(clients.keySet().contains("Null"+ Integer.toString((i-1))));
                                }
                                cc.sendMessage(new MSpam());
                            }
                        }
                    }

                }
                else if (msg instanceof MChat) {
                    String recipient = ((MChat) msg).getRecipientUsername();
                    // In research mode, record times it took for server to receive chat messages from senders
                    if(researchMode) {
                        try (FileWriter fw = new FileWriter(logFileNameServer, true);
                             BufferedWriter bw = new BufferedWriter(fw);
                             PrintWriter out = new PrintWriter(bw);) {

                            if(totalMChatMsgs == 0) {
                                out.println("Sender,Time");
                            }
                            out.print(((MChat) msg).getSenderUsername() + ",");
                            Duration duration = Duration.between(msg.getTimeStamp(), LocalDateTime.now());
                            System.out.println("msg time: " + msg.getTimeStamp());
                            System.out.println("local time: " + LocalDateTime.now());
                            long secondsDelay = Math.abs(duration.getSeconds());
                            long delay = Math.abs(duration.getNano());
                            double total = secondsDelay + (delay/(0.1e10));
                            out.println(total);
                        }
                        catch (IOException e) {
                            System.err.println(e);
                        }
                    }
                    totalMChatMsgs++;
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
                    //the client receives MShutDown message and gracefully shuts down at that
                    // point so no need to tell it to shut down but we need to update our list of clients
                    clients.remove(sender);

                }
                else if(msg instanceof MFailedMessage){
                    MFailedMessage m2 = (MFailedMessage) msg;
                    if (m2.getFailedMessage() instanceof MChat){
                        //todo remove recipient client connection from clients
                        String sender = ((MChat)m2.getFailedMessage()).getSenderUsername();
                        MUnavailable m = new MUnavailable(sender, m2.getOriginalDestination());
                        ClientConnection clientConnection = clients.get(sender);
                        clientConnection.sendMessage(m);
                    }
                }
                else if(msg instanceof MDelayTimes) {
                    // Write results of delay times to a csv file
                    try (FileWriter fw = new FileWriter(logFileName, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw);) {
                        if(totalNumDelayMsgs == 0) {
                            int numMsgs = ((MDelayTimes) msg).getNumMsgs();
                            out.print("Recipient,");
                            for(int i = 1; i <= numMsgs; i++) {
                                out.print(Integer.toString(i) + ',');
                            }
                            out.println("Average");
                        }
                        out.print(((MDelayTimes) msg).getRecipient() + ",");
                        for (String t : ((MDelayTimes) msg).getDelayTimes()) {
                            out.print(t + ",");
                        }
                        out.println(((MDelayTimes) msg).getAvgDelay());
                    }
                    catch (IOException e) {
                        System.err.println(e);
                    }
                    totalNumDelayMsgs++;
                    System.out.println("totalNumDelayMsgs is " + totalNumDelayMsgs);
                    if(totalNumDelayMsgs == totalNumClients/2) {
                        for(String c : clients.keySet()) {
                            ClientConnection cc = clients.get(c);
                            cc.sendMessage(new MStopResearch());
                        }
                    }
                }
                else{
                    System.out.println("message failed to process. " + msg);
                }
        }
        catch (Exception e)
        {
            System.out.println("catching error in processing messages in ChatServer Run");
            e.printStackTrace();
        }
        }
    }

    /**
     * this will shutdown the server gracefully
     */
    public void shutdown()
    {
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

    /**
     * this main function is used to start the server
     * @param args
     */
    public static void main(String[] args){
        if (args.length != 2 && args.length != 4 && args.length != 6)
        {
            System.out.println("Instructions:\n" +
                    "We can run the server in two seperate modes. Regular and Research. \n" +
                    "REGULAR mode instructions:\n" +
                    "~/java -jar ChatServer.jar [Server IP or hostname] [port number]\n" +
                    "\n" +
                    "RESEARCH mode instructions1: \n" +
                    "~/java -jar ChatServer.jar [Server IP or hostname] [port number] research [total clients needed]\n\n" +
                    "RESEARCH mode instructions2: \n" +
                    "~/java -jar ChatServer.jar [Server IP or hostname] [port number] research [total clients needed] [RTT(name).csv] [SenderToReceiver(name).csv] ");
            return;
        }
        ChatServer chatServer=new ChatServer(args[0], Integer.parseInt(args[1]));
        if(args.length >= 4 && args[2].equalsIgnoreCase("research")) {
            chatServer.researchMode = true;
            chatServer.totalNumClients = Integer.parseInt(args[3]);
            System.out.println("Server is running in research mode");
            System.out.println("Research mode is expecting " + chatServer.totalNumClients + " clients to connect");
            if(args.length == 6) {
                chatServer.logFileName = args[4];
                chatServer.logFileNameServer = args[5];
            }
        }

    }
}
