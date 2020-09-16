package Chat.Messages;

public class ClientUserName extends Message{
    private String userName;
    public ClientUserName(String userName)
    {
        super();
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }
}

