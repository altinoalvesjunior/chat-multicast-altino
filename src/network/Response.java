package network;

import enums.Codes;
import java.util.List;
import java.io.Serializable;

public class Response implements Serializable {

    private String message = "";

    private User user = null;
    private List<Room> roomList = null;
    private Room room = null;

    private Codes code = Codes.KEEP_CONNECTION;

    // construtor padrão
    public Response(Codes code, String message, User user) {
        this.code = code;
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public Room getChatRoom() {
        return room;
    }

    public Codes getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
