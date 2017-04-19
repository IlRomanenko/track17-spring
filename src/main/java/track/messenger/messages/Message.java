package track.messenger.messages;

import java.io.Serializable;

/**
 *
 */
public abstract class Message implements Serializable {

    private Long senderId;
    private Type type;

    public Message(Long senderId, Type type) {
        this.senderId = senderId;
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Message{sender: %d type: %s}", senderId, type.toString());
    }
}
