package track.messenger.teacher.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.User;
import track.messenger.messages.Message;
import track.messenger.messages.TextMessage;
import track.messenger.net.BinaryProtocol;
import track.messenger.net.Protocol;
import track.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


/**
 *
 */
public class MessengerClient {


    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     */
    static Logger LOG = LoggerFactory.getLogger(MessengerClient.class);

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
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        /*
      Тред "слушает" сокет на наличие входящих сообщений от сервера
     */
        Thread socketListenerThread = new Thread(() -> {
            LOG.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Здесь поток блокируется на ожидании данных
                    Message msg = protocol.decode(in);
                    if (msg != null) {
                        onMessage(msg);
                    }
                } catch (Exception e) {
                    LOG.error("Failed to process connection: {}", e);
                    e.printStackTrace();
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
        LOG.info("Message received: {}", msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        LOG.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/login":
                // TODO: реализация
                break;
            case "/help":
                // TODO: реализация
                break;
            case "/text":
                // FIXME: пример реализации для простого текстового сообщения
                TextMessage sendMessage = new TextMessage(currentUser.getId());
                sendMessage.setText(tokens[1]);
                send(sendMessage);
                break;
            // TODO: implement another types from wiki

            default:
                LOG.error("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        LOG.info(msg.toString());
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
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
            System.out.println("$");
            while (true) {
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    LOG.error("Failed to process user input", e);
                }
            }
        } catch (Exception e) {
            LOG.error("Application failed.", e);
        } finally {
            if (client != null) {
                // TODO
                client.close();
            }
        }
    }
}