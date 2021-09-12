package app;

import configs.ConnectionConfig;
import enums.Codes;
import network.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
                Response responseSerial = responseMessage(request, message);
                System.out.println("Servidor:" + message);

                byte[] serializedResponse = serverResponseSerializeConvert.serialize(responseSerial);

                DatagramPacket response = new DatagramPacket(serializedResponse, serializedResponse.length, request.getAddress(), request.getPort());
                datagramSocket.send(response);
            }
        } catch (SocketException se) {
            System.out.println("Erro no Socket: " + se.getMessage());
        } catch (IOException e) {
            System.out.print("Erro: ");
            e.printStackTrace();
        } finally {
            if (!(datagramSocket == null))
                datagramSocket.close();
        }
    }

    private static DatagramPacket createRequestDatagramPacket() {
        byte[] buffer = new byte[100000];
        return (new DatagramPacket(buffer, buffer.length));
    }
    private static Room createRoom(String roomName, User user, String groupChatAdress) {

        try {
            boolean findedRoom = false;

            InetAddress address = InetAddress.getByName(groupChatAdress);
            Room newRoom = new Room(roomName, address, user);

            for (Room room : rooms) {
                if (newRoom.equals(room)) {
                    findedRoom = true;
                    break;
                }
            }

            if (!(findedRoom)) {
                newRoom.addNewUser(user);
                rooms.add(newRoom);

                return newRoom;
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Room joinRoom(String roomName, User user) {
        for (Room r : rooms) {
            if (r.getName().equals(roomName)) {
                r.addNewUser(user);
                return r;
            }
        }
        return null;
    }

    private static boolean leaveRoom(String roomName, User user) {
        for (Room r : rooms) {
            if (r.getName().equals(roomName))
                return r.remove(user);
        }

        return false;
    }

    private static Response responseMessage(DatagramPacket request, String input) {

        String[] commandSplit = input.split(" ", 2);
        String command = commandSplit[0];

        String param = null;
        if (commandSplit.length > 1)
            param = commandSplit[1];

        switch (command) {

            case Commands.newRoom:
                if (param != null) {
                    String[] params = param.split(" ", 3);

                    if (params.length != 3) return new Response("Erro ao criar uma nova sala, tente novamente \n tente no seguinte formato: criar [nome da sala] [endereço IP]");

                    String roomName = params[0];
                    String address = params[1];
                    String username = params[2];

                    Room chatRoom = createRoom(roomName,
                            new User(request.getAddress(), request.getPort(), username), address);

                    if (chatRoom != null) return new Response(Codes.START_CHAT, chatRoom);
                    else return new Response("Ops! Já existe uma sala com este nome ou endereço, tente novamente.");

                } else {
                    return new Response("Erro ao criar uma nova sala, tente novamente \n tente no seguinte formato: criar + nome da sala + endereço IP");
                }

            case Commands.start:
                if (param != null) {
                    String[] params = param.split(" ", 1);

                    if (params.length != 1) return new Response("Falha na conexão, tente novamente!");

                    String nickname = params[0];

                    return new Response(Codes.START, "Conectado",
                            new User(request.getAddress(), request.getPort(), nickname));
                } else {
                    return new Response("Falha na conexão, tente novamente!");
                }

            case Commands.join:
                if (param != null) {
                    String[] params = param.split(" ", 2);

                    if (params.length != 2) return new Response("Falha ao ingressar na sala! \n Tente novamente no seguinte formato: entrar + nome da sala");

                    String roomName = params[0];
                    String memberNick = params[1];

                    Room chatRoom = joinRoom(roomName, new User(request.getAddress(), request.getPort(), memberNick));
                    if (chatRoom != null)  return new Response(Codes.START_CHAT, chatRoom);
                    else return new Response("ERRO: Sala não encontrada!");

                } else {
                    return new Response("Falha ao ingressar na sala! \n Tente novamente no seguinte formato: entrar + nome da sala");
                }

            case Commands.leave:
                if (param != null) {
                    String[] params = param.split(" ", 2);

                    if (params.length != 2) return new Response("Ops! Não foi possível sair da sala!");

                    String roomName = params[0];
                    String username = params[1];

                    boolean work = leaveRoom(roomName,
                            new User(request.getAddress(), request.getPort(), username));

                    if (work) return new Response(Codes.END_CHAT);

                } else {
                    return new Response("Ops! Não foi possível sair da sala!");
                }

            case Commands.allRooms:
                return new Response(Codes.LIST_ROOMS, rooms);

            case Commands.end:
                return new Response(Codes.END_CONNECTION, "Conexão finalizada!");

            case Commands.help:
                return new Response("Comandos: \n Criar uma nova sala - /criar + nome da sala + endereço IP (224.0. 0.0 até 239.255.255.255) " +
                        "\n Listar salas disponíveis - /salas " +
                        "\n Ingressar em uma sala - /ingressar + nome da sala" +
                        "\n Sair de uma sala - /sair" +
                        "\n Desconectar do servidor - /desconectar" +
                        "\n Ver membros do chat - /membros" +
                        "\n Ver comandos disponíveis - /ajuda");

            case Commands.members:
                if (param != null) {
                    String[] params = param.split(" ", 2);

                    if (params.length != 1) return new Response("Não foi possível recuperar o membros da sala.");

                    String roomName = params[0];
                    for (Room r : rooms) {
                        if (r.getName().equals(roomName)) return new Response(Codes.MEMBERS, r);
                    }

                    return new Response("Ops, essa sala não foi encontrada!");
                } else {
                    return new Response("Não foi possível recuperar o membros da sala.");
                }

            default:
                return new Response("Ixi, o comando digitado é inexistente, tente novamente!");
        }
    }
}
