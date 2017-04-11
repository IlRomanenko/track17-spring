package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.commands.Command;
import track.messenger.commands.CommandFactory;
import track.messenger.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MessengerServer {

    private static Logger LOG = LoggerFactory.getLogger(MessengerServer.class);

    private Protocol protocol;
    private Thread acceptConnThread;

    private ExecutorService commandsPool;

    private volatile LinkedList<Session> sessions = new LinkedList<>();

    private volatile boolean isAlive = true;

    public MessengerServer(int threadsCount, Protocol protocol) {
        this.protocol = protocol;
        commandsPool = Executors.newFixedThreadPool(threadsCount);
    }

    public void start(int port) {
        acceptConnThread = new Thread(() -> {
            LOG.info(String.format("Start server on port %d", port));
            try {
                acceptConnections(port);
            } catch (IOException ex) {
                LOG.error(ex.getMessage());
                isAlive = false;
            }
        });
        acceptConnThread.start();

        while (isAlive) {
            if (sessions.isEmpty()) {
                continue;
            }
            Session session = sessions.getFirst();
            try {
                Message msg = session.readMessage();
                if (msg == null) {
                    continue;
                }
                Command cmd = CommandFactory.create(msg.getType());
                if (cmd == null) {
                    continue;
                }
                commandsPool.submit(() -> cmd.execute(session, msg));
            } catch (ProtocolException e) {
                LOG.error(e.getMessage());
            }
            sessions.push(session);
        }

        LOG.info("Stop server");
        acceptConnThread.stop();
    }

    private void acceptConnections(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket client = serverSocket.accept();
            LOG.info("Accept connection");
            sessions.add(new Session(client, protocol));
        }
    }

}
