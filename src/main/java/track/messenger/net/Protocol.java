package track.messenger.net;

import track.messenger.messages.Message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 */
public interface Protocol {

    default Message decode(byte[] bytes) throws ProtocolException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return decode(bais);
    }

    Message decode(InputStream stream) throws ProtocolException;

    Integer decodeInteger(InputStream stream);

    Integer decodeInteger(byte[] array);

    byte[] encode(Message msg);

    byte[] encode(Integer integer);

    byte[] serialize(Message msg);

    Message deserialize(InputStream in) throws ProtocolException;
}
