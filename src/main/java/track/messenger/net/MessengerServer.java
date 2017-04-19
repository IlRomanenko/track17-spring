package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 */
@Component
public class MessengerServer {

    private static Logger log = LoggerFactory.getLogger(MessengerServer.class);

    private Thread acceptConnThread;
    private ServerSocket acceptSocket;
    private volatile SessionsHandler sessionsHandler;

    @Value("${track.messenger.net.port:4242}")
    private int port;

    @Value("${track.messenger.net.threads:1}")
    private int threadsCount;

    private Protocol protocol;

    @PostConstruct
    public void postConstruct() {
        this.protocol = new BinaryProtocol();
        sessionsHandler = new SessionsHandler(protocol, threadsCount);
    }

    public void start() {
        acceptConnThread = new Thread(() -> {
            log.info(String.format("Start server on port %d", port));
            try {
                acceptConnections(port);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        });
        acceptConnThread.start();

        sessionsHandler.start();

        log.info("Stop server");
        try {
            acceptSocket.close();
            acceptConnThread.interrupt();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

    }

    private void acceptConnections(int port) throws IOException {
        acceptSocket = new ServerSocket(port);

        while (true) {
            try {
                Socket clientSocket = acceptSocket.accept();
                log.info("Accept connection");
                sessionsHandler.createSession(clientSocket);

            } catch (SocketException ex) {
                log.info("Accept server has stopped");
                break;
            }
        }
    }

}
