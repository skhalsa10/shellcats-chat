package Chat.Messages;

import java.util.ArrayList;

/**
 * Message sent from recipient client to server reporting delay times of messages
 * used for research mode
 */
public class MDelayTimes extends Message {
    private String sender;
    private String recipient;
    private ArrayList<Double> delayTimes;
    private double avgDelay;
    private int numMsgs;

    public MDelayTimes(String sender, String recipient,  ArrayList<Double> delayTimes, double avgDelay, int numMsgs) {
        super();
        this.sender = sender;
        this.recipient = recipient;
        this.delayTimes = delayTimes;
        this.avgDelay = avgDelay;
        this.numMsgs = numMsgs;
    }

    /**
     * Gets the username of the recipient
     * @return recipient username
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Gets the username of the sender
     * @return sender username
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the list of delay times for each of the messages sent in research mode
     * @return Array list of delay times
     */
    public ArrayList<Double> getDelayTimes() {
        return delayTimes;
    }

    /**
     * Gets the average delay time of the messages
     * @return average delay time
     */
    public Double getAvgDelay() {
        return avgDelay;
    }

    /**
     * Gets the total number of messages that each sender client is supposed to send
     * @return number of messages
     */
    public int getNumMsgs() {
        return numMsgs;
    }
}
