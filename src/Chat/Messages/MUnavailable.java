package Chat.Messages;

/**
 * Message to notify client that the other client is unavailable
 */
public class MUnavailable extends Message{
    private final String sender;
    private final String recipient;

    public MUnavailable(String sender, String recipient) {
        super();
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * Gets the username of the sender
     * @return sender username
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the username of the recipient
     * @return
     */
    public String getRecipient() {
        return recipient;
    }
}
