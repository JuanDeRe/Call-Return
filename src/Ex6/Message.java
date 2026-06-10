package Ex6;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private String sender;
    private String text;

    public Message(int id, String sender, String text) {
        this.id = id;
        this.sender = sender;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

}
