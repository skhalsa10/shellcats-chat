package Chat.Messages;

/**
 * Message containing the actual chat messages between clients
 */
public class MChat extends Message {
    private final String senderUsername;
    private final String recipientUsername;
    private final String chatMessage;

    public MChat(String senderUsername, String recipientUsername, String chatMessage) {
        super();
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.chatMessage = chatMessage;
    }

    /**
     * Gets the chat message
     * @return chat message to recipient client
     */
    public String getChatMessage() {
        return chatMessage;
    }

    /**
     * Gets username of recipient
     * @return recipient username
     */
    public String getRecipientUsername() {
        return recipientUsername;
    }

    /**
     * Gets username of sender
     * @return sender username
     */
    public String getSenderUsername() {
        return senderUsername;
    }
}
