package track.messenger.net;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.messages.Message;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tehnotrack
 * track.messenger.net
 * <p>
 * Created by ilya on 10.04.17.
 */
public class BinaryProtocol implements Protocol {

    private static Logger LOG = LoggerFactory.getLogger(BinaryProtocol.class);

    @Override
    public Message decode(InputStream stream) throws ProtocolException {
        Message message = null;
        try {
            if (stream.available() > 0) {
                message  = SerializationUtils.deserialize(stream);
                LOG.info("Decoded ", message);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ProtocolException(e.getMessage());
        }
        return message;
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        LOG.info("Encoded ", msg);
        return SerializationUtils.serialize(msg);
    }
}
