package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.messages.Message;
import track.messenger.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session {

    private static final int DEFAULT_CAPACITY = 1024;
    private static Logger log = LoggerFactory.getLogger(Session.class);
    private static volatile long SESSION_ID = 0;
    private static final int SOCKET_TIMEOUT = 1;

    private User user;
    private Socket socket;

    private SessionsHandler handler;

    private long id;

    private boolean alive = true;
    private Protocol protocol;

    private InputStream in;
    private OutputStream out;

    private ByteBuffer buffer;
    private int remainsCount;
    private boolean hasReadLength;

    public Session(Socket socket, Protocol protocol, SessionsHandler handler) {
        this.id = SESSION_ID++;

        this.socket = socket;
        this.handler = handler;

        this.protocol = protocol;
        try {
            this.socket.setSoTimeout(SOCKET_TIMEOUT);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        buffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
        remainsCount = Integer.BYTES;
        hasReadLength = false;
        buffer.mark();
    }

    public void send(Message msg) {
        byte[] bytes = protocol.encode(msg);
        try {
            out.write(protocol.encode(bytes.length));
            out.write(bytes);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            alive = false;
        }
    }

    public Message readMessage() throws ProtocolException {
        Message msg = null;
        try {
            if (!hasReadLength) {
                int currentRead = in.read(buffer.array(), buffer.position(), remainsCount);
                if (currentRead == -1) {
                    alive = false;
                    return null;
                }
                remainsCount -= currentRead;
                if (remainsCount != 0) {
                    return null;
                }

                remainsCount = protocol.decodeInteger(buffer.array());
                buffer.reset();
                hasReadLength = true;
            }
            int currentRead = in.read(buffer.array(), buffer.position(), remainsCount);

            if (currentRead == -1) {
                alive = false;
                return null;
            }
            remainsCount -= currentRead;
            if (remainsCount == 0) {
                msg = protocol.decode(buffer.array());
                buffer.reset();
                hasReadLength = false;
                remainsCount = Integer.BYTES;
            }
        } catch (SocketTimeoutException ex) {
            msg = null;
        } catch (IOException e) {
            alive = false;
            log.error(e.getMessage());
        }
        return msg;
    }

    public long getId() {
        return id;
    }

    public void close() {
        try {
            log.warn("......Close session!......");
            socket.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SessionsHandler getHandler() {
        return handler;
    }
}