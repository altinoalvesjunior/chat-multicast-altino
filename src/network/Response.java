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

    // construtor para código
    public Response(Codes code) {
        this.code = code;
    }

    // construtor para mensagem
    public Response(String message) {
        this.message = message;
    }

    // construtor para código e messagem
    public Response(Codes code, String message) {
        this.code = code;
        this.message = message;
    }

    // construtor para código e lista de salas
    public Response(Codes code, List<Room> roomList) {
        this.code = code;
        this.roomList = roomList;
    }

    // construtor para código e sala
    public Response(Codes code, Room chatRoom) {
        this.code = code;
        this.room = chatRoom;
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
