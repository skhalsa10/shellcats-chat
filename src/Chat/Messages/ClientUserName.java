package Chat.Messages;

public class ClientUserName extends Message{
    private String userName;
    private String tempUserName;

    public ClientUserName(String userName)
    {
        super();
        this.userName=userName;
        tempUserName = null;

    }

    public String getTempUserName() {
        return tempUserName;
    }

    public void setTempUserName(String tempUserName) {
        this.tempUserName = tempUserName;
    }


    public String getUserName() {
        return userName;
    }
}
