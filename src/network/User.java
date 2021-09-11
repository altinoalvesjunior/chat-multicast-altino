package network;

import java.net.InetAddress;
import java.util.Objects;
import java.io.Serializable;

public class User implements Serializable {

    private InetAddress address;
    private int port;
    private String name;

    public User(InetAddress address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass() || obj == null) {
            return false;
        }

        User user = (User) obj;

        return Objects.equals(address, user.address) &&
                Objects.equals(port, user.port) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, name);
    }
}
