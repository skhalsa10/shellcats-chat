package Chat.Messages;

/**
 * Message to notify client that choosen username exists
 */
public class MUsernameExists extends Message {
    private final String username;
    private String msg;
    private String tempName;

    public MUsernameExists(String username, String tempName) {
        super();
        this.username = username;
        this.tempName = tempName;
        this.msg = "The username " + username + " already exists! Enter another username!!\n";
    }

    /**
     * Gets the message
     * @return username exists message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Gets the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the client's temp username
     * @return temp username of client
     */
    public String getTempName() {
        return tempName;
    }
}
