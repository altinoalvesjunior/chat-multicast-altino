package app;

import configs.ConnectionConfig;
import enums.Codes;
import network.*;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class Client {
    static final SerializeConversor<Message> chatMessageSerializeConvert = new SerializeConversor<>();
    static final SerializeConversor<Response> serverResponseSerializeConvert = new SerializeConversor<>();

    static final Scanner sc = new Scanner(System.in);
    static String username;
    static InetAddress inetAddress;

    static byte[] buffer = new byte[100000];

    static DatagramPacket request;
    static DatagramPacket response = new DatagramPacket(buffer, buffer.length);

    static Response serverResponse;
    static DatagramSocket datagramSocket;

    static Room room;
    static User nickname;

    public static void main(String[] args) {

        try {
            datagramSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName("localhost");

            System.out.println("Olá, seja bem-vindo(a)!");
            System.out.print("Para começarmos, digite seu nome ou apelido: ");
            username = sc.next();

            String initialCommand = Commands.start + " " + username;
            request = new DatagramPacket(initialCommand.getBytes(), initialCommand.length(), inetAddress, ConnectionConfig.PORT);

            sendMessagesServer();

            if (Codes.START != serverResponse.getCode()) {
                return;
            }

            sc.nextLine();
            do {
                System.out.print("\nComando: ");
                String text = sc.nextLine();
                System.out.println();
                text = text + " " + username;

                request = new DatagramPacket(text.getBytes(), text.length(), inetAddress, ConnectionConfig.PORT);
                sendMessagesServer();
            } while (serverResponse.getCode() != Codes.END_CONNECTION);

        } catch (SocketException e) {
            System.out.print("ERRO Socket: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        } finally {
            if (datagramSocket != null)
                datagramSocket.close();
        }
    }

    private static void actions() {

        Codes code = serverResponse.getCode();

        switch (code) {
            case START:
                nickname = serverResponse.getUser();
                System.out.println(serverResponse.getMessage());
                break;

            case START_CHAT:
                startChatRoom();
                break;

            case KEEP_CONNECTION:
                System.out.println(serverResponse.getMessage());
                break;

            case LIST_ROOMS:
                List<Room> availableRooms = serverResponse.getRoomList();

                System.out.println("--- Salas Disponíveis ---");
                System.out.println(availableRooms);
                break;

            case MEMBERS:
                Room room = serverResponse.getChatRoom();

                System.out.println("On-line agora: " + room.getMembers());
                System.out.println("-------------------------------");
                break;

            case HELP: {
                System.out.println("Teste");
            }

            case END_CONNECTION:

            case END_CHAT:
                System.out.println("Você foi desconectado(a)!");
                break;

            default:
                System.out.println("Algo deu errado!");
                break;
        }
    }

    private static void sendMessagesServer() throws IOException {
            datagramSocket.send(request);
            datagramSocket.receive(response);
            serverResponse = serverResponseSerializeConvert.deserialize(response.getData());

            actions();
    }

    private static void startChatRoom() {
        
    }
}