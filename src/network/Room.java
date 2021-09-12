package network;

import java.io.Serializable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Room implements Serializable {

    static final SerializeConversor<Message> chatMessageSerializeConvert = new SerializeConversor<>();

    final private User user;
    final private String name;
    private InetAddress address;

    private List<User> chatUsers = new ArrayList<>();

    private AtomicBoolean working = new AtomicBoolean(false);

    public Room(String name, InetAddress address, User user) {
        this.name = name;
        this.address = address;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public List<User> getMembers() {
        return chatUsers;
    }

    public void addNewUser(User user) {
        chatUsers.add(user);
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) {
            return true;
        }

        if(getClass() != obj.getClass() || obj == null) {
            return false;
        }

        Room chatRoom = (Room) obj;
        return name.equals(chatRoom.name);
    }

    public void listenRoom(MulticastSocket multicastSocket) {
        working.set(true);

        new Thread(() -> {
            while (working.get()) {
                byte[] buffer = new byte[100000];
                DatagramPacket in = new DatagramPacket(buffer, buffer.length);

                try {
                    multicastSocket.receive(in);
                    Message message = chatMessageSerializeConvert.deserialize(in.getData());

                    System.out.println(message.getUsername() + " disse: " + message.getPhrase());
                } catch (IOException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }).start();
    }

    public void stopRoom() { working.set(false); }

    public boolean remove(User user) {
        return this.getMembers().removeIf(
                (member) -> member.equals(user)
        );
    }
}