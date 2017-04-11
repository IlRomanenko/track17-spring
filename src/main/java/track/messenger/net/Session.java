package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.User;
import track.messenger.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session {

    static Logger LOG = LoggerFactory.getLogger(Session.class);

    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;

    // сокет на клиента
    private Socket socket;

    private Protocol protocol;
    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    public Session(Socket socket, Protocol protocol) {
        this.socket = socket;
        this.protocol = protocol;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public void send(Message msg) throws ProtocolException, IOException {
        out.write(protocol.encode(msg));
    }

    public Message readMessage() throws ProtocolException {
        return protocol.decode(in);
    }

    public void close() {
        // TODO: закрыть in/out каналы и сокет. Освободить другие ресурсы, если необходимо
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
    }
}