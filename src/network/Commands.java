package network;

public class Commands {
    public static final String start = "/start"; // iniciar o chat - nunca chamado no pelo cliente
    public static final String join = "/ingressar"; // ingressar em uma sala, quando cliente chamar deve-se colocar /ingressar + nome da sala
    public static final String end = "/desconectar"; // desconectar do servidor
    public static final String leave = "/sair"; // sair de uma sala de bate-papo
    public static final String newRoom = "/criar"; // criar uma nova sala, quando cliente chamar deve-se colocar no seguinte formato /criar + nome da sala + endereço IP
    public static final String members = "/membros"; // lista membros do chat
    public static final String allRooms = "/salas"; // exibe todas salas disponíveis
    public static final String help = "/ajuda"; // chama menu de ajuda
}
