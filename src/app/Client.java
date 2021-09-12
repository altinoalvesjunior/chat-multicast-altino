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

            System.out.println();
            System.out.println("Digite '/ajuda' para ver os comandos disponíveis");
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
                startChat();
            break;

            case KEEP_CONNECTION:
                System.out.println(serverResponse.getMessage());
            break;

            case LIST_ROOMS:
                List<Room> available = serverResponse.getRoomList();

                System.out.println("--- Salas disponíveis ---");
                System.out.println(available);
            break;

            case MEMBERS:
                Room room = serverResponse.getChatRoom();

                System.out.println("On-line agora: " + room.getMembers());
                System.out.println("-------------------------------");
           break;

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

    private static void startChat() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(7500);

            boolean finish = false;

            room = serverResponse.getChatRoom();
            InetAddress multicastAddress = room.getAddress();

            multicastSocket.joinGroup(multicastAddress);

            System.out.println("-----------" + room.getName() + "-----------");
            System.out.println();
            System.out.println("-------------------------");
            System.out.println("Pessoas on-line agora: " + room.getMembers());
            System.out.println("-------------------------");

            String message;
            room.listenRoom(multicastSocket);

            do {
                message = sc.nextLine();
                System.out.print("> ");

                if (message.charAt(0) != '/') {
                    byte[] bytes = chatMessageSerializeConvert.serialize(new Message(nickname, message));

                    DatagramPacket output = new DatagramPacket(bytes, bytes.length, multicastAddress, 7500);
                    multicastSocket.send(output);
                }

                else {
                    String[] messageSplit = message.split(" ", 2);
                    String commandSelected = messageSplit[0];

                    switch (commandSelected) {

                        case Commands.members:
                            String getMembers = commandSelected + " " + room.getName();
                            request = new DatagramPacket(getMembers.getBytes(), getMembers.length(), inetAddress, ConnectionConfig.PORT);
                            sendMessagesServer();
                        break;

                        case Commands.leave:
                            room.stopRoom();

                            byte[] bytes = chatMessageSerializeConvert.serialize(new Message(nickname, "Desconectando!"));

                            DatagramPacket messageOutput = new DatagramPacket(bytes, bytes.length, multicastAddress, ConnectionConfig.TESTE);

                            multicastSocket.send(messageOutput);
                            multicastSocket.leaveGroup(multicastAddress);

                            String logoffCommand = commandSelected + " " + room.getName() + " " + username;
                            request = new DatagramPacket(logoffCommand.getBytes(), logoffCommand.length(), inetAddress, ConnectionConfig.PORT);

                            sendMessagesServer();
                            finish = true;
                        break;

                        default:
                            System.out.println("O comando digitado não existe, tente novamente!");
                        break;
                    }
                }
            } while (!(finish));

        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}