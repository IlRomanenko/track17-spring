package track.messenger.net;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * Tehnotrack
 * track.messenger.net
 * <p>
 * Created by ilya on 10.04.17.
 */
public class BinaryProtocol implements Protocol {

    private static Logger log = LoggerFactory.getLogger(BinaryProtocol.class);

    @Override
    public Message decode(InputStream stream) throws ProtocolException {
        Message message;
        try {
            ObjectInputStream ois = new ObjectInputStream(stream);
            message = (Message)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new ProtocolException(e);
        }
        log.info("Decoded ", message);
        return message;
    }

    @Override
    public Integer decodeInteger(InputStream stream) {
        byte[] buffer = new byte[Integer.BYTES];
        Integer result;
        try {
            if (stream.read(buffer) == Integer.BYTES) {
                result = decodeInteger(buffer);
            } else {
                result = -1;
            }
        } catch (IOException ex) {
            result = -1;
        }
        return result;
    }

    @Override
    public Integer decodeInteger(byte[] array) {
        Integer result = 0;
        Integer maxByteValue = 1 << Byte.SIZE;
        for (int i = 0; i < Integer.BYTES; i++) {
            result = (result << Byte.SIZE) + (array[i] + maxByteValue) % maxByteValue;
        }
        return result;
    }

    @Override
    public byte[] encode(Message msg) {
        log.info("Encoded ", msg);
        byte[] result = null;
        try {
            result = SerializationUtils.serialize(msg);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return result;
    }

    @Override
    public byte[] encode(Integer value) {
        byte[] bytes = new byte[Integer.BYTES];
        for (int i = 0; i < Integer.BYTES; i++) {
            bytes[Integer.BYTES - i - 1] = value.byteValue();
            value >>= Byte.SIZE;
        }
        return bytes;
    }

    @Override
    public byte[] serialize(Message msg) {
        byte[] msgSer = encode(msg);
        byte[] lenSer = encode(msgSer.length);
        return ByteBuffer.allocate(msgSer.length + lenSer.length).put(lenSer).put(msgSer).array();
    }

    @Override
    public Message deserialize(InputStream in) throws ProtocolException {
        decodeInteger(in);
        return decode(in);
    }
}
