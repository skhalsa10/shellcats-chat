package Chat.Messages;

import java.util.ArrayList;

/**
 * Message sent from recipient client to server reporting delay times of messages
 * used for research mode
 */
public class MDelayTimes extends Message {
    private String sender;
    private String recipient;
    private ArrayList<Long> delayTimes;
    private Long avgDelay;

    public MDelayTimes(String sender, String recipient,  ArrayList<Long> delayTimes, Long avgDelay) {
        super();
        this.sender = sender;
        this.recipient = recipient;
        this.delayTimes = delayTimes;
        this.avgDelay = avgDelay;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<Long> getDelayTimes() {
        return delayTimes;
    }

    public Long getAvgDelay() {
        return avgDelay;
    }
}
