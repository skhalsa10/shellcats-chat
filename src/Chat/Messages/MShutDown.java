package Chat.Messages;

/**
 * Message sent by client to notify server that it is shutting down.
 */
public class MShutDown extends Message{
    private final String username;

    public MShutDown(String username) {
        super();
        this.username = username;
    }

    /**
     * Gets the username of the client who wishes to shut down
     * @return client username
     */
    public String getUsername() {
        return username;
    }
}
