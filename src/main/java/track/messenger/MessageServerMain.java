package track.messenger;

import track.messenger.net.BinaryProtocol;
import track.messenger.net.MessengerServer;

/**
 *
 */
public class MessageServerMain {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage <port> ");
            return;
        }

        int port = Integer.parseInt(args[0]);

        MessengerServer server = new MessengerServer(2, new BinaryProtocol());

        server.start(port);

    }
}
