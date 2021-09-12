package network;

import java.io.Serializable;

public class Message implements Serializable {

    final private String phrase;
    final private User username;

    public Message(User username, String phrase) {
        this.username = username;
        this.phrase = phrase;
    }

    public User getUsername() {
        return username;
    }

    public String getPhrase() {
        return phrase;
    }
}

