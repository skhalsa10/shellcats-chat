package Chat.Messages;

public class MFailedMessage extends Message {

    private Message failedMessage;
    private String originalDestination;

    public MFailedMessage(Message failedMessage,  String originalDestination){
        this.failedMessage = failedMessage;
        this.originalDestination = originalDestination;
    }

    public Message getFailedMessage() {
        return failedMessage;
    }

    public String getOriginalDestination() {
        return originalDestination;
    }
}
