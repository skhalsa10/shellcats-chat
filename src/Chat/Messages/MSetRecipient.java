package Chat.Messages;

public class MSetRecipient extends Message {

    private String recipient;


    public MSetRecipient(String recipient){
        super();
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

}
