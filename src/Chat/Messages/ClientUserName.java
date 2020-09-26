package Chat.Messages;

/**
 * Message sent from client to server specifying its name
 */
public class ClientUserName extends Message{
    private String userName;
    private String tempUserName;

    public ClientUserName(String userName)
    {
        super();
        this.userName=userName;
        tempUserName = null;

    }

    /**
     * Gets the temp username
     * @return temp username
     */
    public String getTempUserName() {
        return tempUserName;
    }

    /**
     * Sets the temp username
     * @param tempUserName temp username
     */
    public void setTempUserName(String tempUserName) {
        this.tempUserName = tempUserName;
    }

    /**
     * Gets the new username
     * @return new username
     */
    public String getUserName() {
        return userName;
    }
}
