package track.lections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

class TCPEchoServer {
    private static final int BUFSIZE = 32;


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        Selector selector = SelectorProvider.provider().openSelector();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverChannel.socket().bind(new InetSocketAddress("localhost", servPort));

        serverChannel.register(selector, serverChannel.validOps());


        ByteBuffer recvBuffer = ByteBuffer.allocate(BUFSIZE);

        while (selector.select() > 0) {


            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                SocketChannel clientChannel;

                if (key.isAcceptable()) {
                    ServerSocketChannel clientServerChannel = (ServerSocketChannel) key.channel();
                    clientChannel = clientServerChannel.accept();

                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, clientChannel.validOps());

                } else if (key.isReadable()) {
                    clientChannel = (SocketChannel)key.channel();
                    clientChannel.read(recvBuffer);
                    recvBuffer.flip();
                    clientChannel.write(recvBuffer);
                    clientChannel.close();
                }
                iterator.remove();
            }
        }
    }
}
