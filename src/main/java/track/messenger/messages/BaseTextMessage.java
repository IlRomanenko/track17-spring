package track.messenger.messages;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 19.04.17.
 */
public class BaseTextMessage extends Message {

    protected String text;

    public BaseTextMessage(Long senderId, String text) {
        super(senderId, Type.MSG_TEXT);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
