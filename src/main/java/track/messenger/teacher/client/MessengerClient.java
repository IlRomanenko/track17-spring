package track.messenger.teacher.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.messages.Message;
import track.messenger.messages.Type;
import track.messenger.messages.requests.*;
import track.messenger.messages.responses.*;
import track.messenger.models.User;
import track.messenger.net.BinaryProtocol;
import track.messenger.net.Protocol;
import track.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 *
 */
public class MessengerClient {


    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     */
    static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    /**
     * Протокол, хост и порт инициализируются из конфига
     */
    private Protocol protocol;
    private int port;
    private String host;
    private User currentUser = null;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        Thread socketListenerThread = new Thread(() -> {
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message msg = protocol.deserialize(in);
                    if (msg != null) {
                        onMessage(msg);
                    }
                } catch (ProtocolException e) {
                    log.error("Failed to process connection: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        log.info("Message received: {}", msg);

        if (msg.getType() == Type.MSG_SEND_TEXT) {
            TextMessage textMessage = (TextMessage) msg;
            System.out.println(String.format("Message from %d : %s", textMessage.getSenderId(), textMessage.getText()));
            return;
        }

        StatusMessage status = (StatusMessage) msg;
        if (status.getStatus() == StatusMessage.Status.OK) {
            log.info("Operation succeeded");

            switch (msg.getType()) {

                case MSG_STATUS:
                    //... do nothing
                    break;

                case MSG_CHAT_LIST_RESULT:
                    ChatListResultMessage chatListMessage = (ChatListResultMessage) msg;
                    for (Map.Entry<Long, String> entry : chatListMessage.getChats()) {
                        System.out.print(String.format("--- %d : %s ---  ", entry.getKey(), entry.getValue()));
                    }
                    System.out.println();
                    break;

                case MSG_CHAT_HIST_RESULT:
                    ChatHistoryResultMessage chatHistoryMessage = (ChatHistoryResultMessage) msg;
                    for (Long id : chatHistoryMessage.getMessagesId()) {
                        System.out.print(String.format("%d, ", id));
                    }
                    System.out.println();
                    break;

                case MSG_INFO_RESULT:
                    InfoResultMessage infoMessage = (InfoResultMessage) msg;
                    currentUser = infoMessage.getUser();
                    break;

                case MSG_USERS_LIST_RESULT:
                    UserListResultMessage userListMessage = (UserListResultMessage) msg;
                    System.out.println("Users : ");
                    for (User user : userListMessage.getUsers()) {
                        System.out.print(String.format("--- %d : %s ---  ", user.getId(), user.getNickname()));
                    }
                    System.out.println();
                    break;

                default:
                    log.error("Catch unknown message from server!");
            }

        } else {
            log.info(String.format("Operation failed because of : {}", status.getStatus()));
        }
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        Long chatId = -1L;

        switch (cmdType) {
            case "/login":
                if (!checkLoggedIn(false)) {
                    break;
                }
                if (tokens.length != 3) {
                    System.out.println("Need <login> <password>");
                    break;
                }
                LoginMessage loginMessage = new LoginMessage(tokens[1], tokens[2]);
                send(loginMessage);

                break;

            case "/createuser":
                if (!checkLoggedIn(false)) {
                    break;
                }
                if (tokens.length != 3) {
                    System.out.println("Need <login> <password>");
                    break;
                }
                UserCreateMessage userCreateMessage = new UserCreateMessage(tokens[1], tokens[2]);
                send(userCreateMessage);
                break;

            case "/userslist":
                send(new UserListMessage());
                break;

            case "/help":
                System.out.println("Alright, this is help string.");
                break;

            case "/text":
                if (!checkLoggedIn(true)) {
                    break;
                }

                try {
                    chatId = Long.parseLong(tokens[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Can't parse chatId");
                    break;
                }

                ChatMessage sendMessage = new ChatMessage(currentUser.getId(), chatId, tokens[2]);
                send(sendMessage);
                break;

            case "/info":

                //TODO !!!
                log.error("INFO NOT IMPLEMENTED");
                break;

            case "/chatslist":

                if (!checkLoggedIn(true)) {
                    break;
                }

                ChatListMessage chatListMessage = new ChatListMessage(currentUser.getId());
                send(chatListMessage);
                break;

            case "/createchat":
                if (!checkLoggedIn(true)) {
                    break;
                }
                if (tokens.length != 3) {
                    System.out.println("Need <chat title> <users ids>");
                    break;
                }

                List<Long> users = Arrays.stream(tokens[2].split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                ChatCreateMessage chatCreateMessage = new ChatCreateMessage(currentUser.getId(), tokens[1], users);
                send(chatCreateMessage);
                break;

            case "/chathist":

                if (!checkLoggedIn(true)) {
                    break;
                }

                if (tokens.length != 2) {
                    System.out.println("Need <chat id>");
                    break;
                }
                try {
                    chatId = Long.parseLong(tokens[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Can't parse chatId");
                    break;
                }

                ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage(currentUser.getId(), chatId);
                send(chatHistoryMessage);

                break;


            default:
                log.error("Invalid input: " + line);
        }
    }

    private boolean checkLoggedIn(boolean need) {
        if (need) {
            if (currentUser == null) {
                System.out.println("Need to be logged in");
                return false;
            }
            return true;
        } else {
            if (currentUser != null) {
                System.out.println("You are already logged in");
                return false;
            }
            return true;
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        out.write(protocol.serialize(msg));
        out.flush();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        client.setHost("localhost");
        client.setPort(4242);
        client.setProtocol(new BinaryProtocol());

        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("$ ");
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    log.error("Failed to process user input", e);
                }
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            // TODO
            log.info("Close socket");
            client.close();

        }
    }
}