package track.messenger.messages;

import java.io.Serializable;

/**
 *
 */
public abstract class Message implements Serializable {

    private static volatile long ID_COUNTER = 0;

    private Long id;
    private Long senderId;
    private Type type;

    public Message(Long senderId, Type type) {
        id = ID_COUNTER++;
        this.senderId = senderId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
