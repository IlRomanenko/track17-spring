package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.commands.Command;
import track.messenger.commands.CommandFactory;
import track.messenger.messages.Message;
import track.messenger.messages.Type;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.messages.responses.TextMessage;
import track.messenger.models.User;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Tehnotrack
 * track.messenger.net
 * <p>
 * Created by ilya on 12.04.17.
 */

public class SessionsHandler {

    private static Logger log = LoggerFactory.getLogger(SessionsHandler.class);

    private Protocol protocol;
    private ExecutorService executorPool;

    private volatile LinkedList<Session> sessions;
    private Map<User, Session> authSessions = new ConcurrentHashMap<>();
    private Map<Long, Session> sessionIDMap = new ConcurrentHashMap<>();

    public SessionsHandler(Protocol protocol, int threadsCount) {
        this.protocol = protocol;
        sessions = new LinkedList<>();
        executorPool = Executors.newFixedThreadPool(threadsCount);
    }

    public void start() {
        while (true) {
            if (sessions.isEmpty()) {
                continue;
            }

            Session session = sessions.removeLast();
            if (!session.isAlive()) {
                closeSession(session.getId());
                continue;
            }

            sessions.push(session);

            Message message = receiveMessage(session);
            if (!session.isAlive()) {
                log.error("Session dead");
            }
            if (message == null) {
                continue;
            }

            executorPool.submit(() -> proceedMessage(session, message));
        }
    }

    private void proceedMessage(Session session, Message msg) {
        log.info("Proceed message : " + msg.toString());

        Command cmd = CommandFactory.getInstance().create(msg.getType());

        if (cmd == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
        } else {
            cmd.execute(session, msg);
        }
    }

    private Message receiveMessage(Session session) {
        Message msg = null;
        try {
            msg = session.readMessage();
        } catch (ProtocolException e) {
            if (session.isAlive()) {
                // TODO send status message
                log.warn("Protocol exception; send StatusMessage ");
                session.send(new StatusMessage(StatusMessage.Status.FAIL));
            }
            log.warn(e.getMessage());
        }
        return msg;
    }

    private void closeSession(long sessionId) {
        Session session = sessionIDMap.get(sessionId);
        if (session.getUser() != null) {
            log.info("Deauth user : " + session.getUser().toString());
            authSessions.remove(session.getUser());
        }
        sessionIDMap.remove(sessionId);
        session.close();
        log.info("Close session : " + session.toString());
    }

    public void createSession(Socket socket) {
        Session session = new Session(socket, protocol, this);
        sessionIDMap.put(session.getId(), session);
        sessions.push(session);

        log.info("Create session : " + session.toString());
    }

    public void authSession(long sessionId, User user) throws SessionException {
        synchronized (authSessions) {
            if (authSessions.containsKey(user)) {
                throw new SessionException("This user has already logged in");
            }
            authSessions.put(user, sessionIDMap.get(sessionId));
            log.info("Auth user : " + user.toString());
        }
    }

    public Map<User, Session> getAuthSessions() {
        return authSessions;
    }

    public void sendMessageInChat(Session session, TextMessage storedMessage) {
        Command cmd = CommandFactory.getInstance().create(Type.MSG_SEND_TEXT);
        cmd.execute(session, storedMessage);
    }
}
