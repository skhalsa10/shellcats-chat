package Chat.Messages;

/**
 * Notifies that a message is unable to be sent
 */
public class MFailedMessage extends Message {

    private Message failedMessage;
    private String originalDestination;

    public MFailedMessage(Message failedMessage,  String originalDestination){
        this.failedMessage = failedMessage;
        this.originalDestination = originalDestination;
    }

    /**
     * Gets the message that failed to be sent
     * @return failed message
     */
    public Message getFailedMessage() {
        return failedMessage;
    }

    /**
     * Gets the original destination of the message
     * @return destination of original message
     */
    public String getOriginalDestination() {
        return originalDestination;
    }
}
