package Chat.Messages;

/**
 * Sets the recipient of a client
 */
public class MSetRecipient extends Message {
    private String recipient;

    public MSetRecipient(String recipient){
        super();
        this.recipient = recipient;
    }

    /**
     * Gets the username of the recipient
     * @return recipient username
     */
    public String getRecipient() {
        return recipient;
    }

}
