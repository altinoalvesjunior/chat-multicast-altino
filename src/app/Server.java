package app;

import configs.ConnectionConfig;
import network.Response;
import network.Room;
import network.SerializeConversor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static final SerializeConversor<Response> serverResponseSerializeConvert = new SerializeConversor<>();

    static List<Room> rooms = new ArrayList<>();
    static DatagramSocket datagramSocket = null;

    public static void main(String[] args) {

        String message;

        try {
            datagramSocket = new DatagramSocket(ConnectionConfig.PORT);
            System.out.println("O servidor está sendo executado na porta " + ConnectionConfig.PORT);

            while (true) {
                DatagramPacket request = createRequestDatagramPacket();
                datagramSocket.receive(request);

                message = new String(request.getData()).trim();
                // criar método ou colocar um switch para opções (pensar em como fazer)
                System.out.println("Servidor:" + message);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DatagramPacket createRequestDatagramPacket() {
        byte[] buffer = new byte[100000];
        return (new DatagramPacket(buffer, buffer.length));
    }


}
