package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages.requests
 * <p>
 * Created by ilya on 15.04.17.
 */
public class InfoMessage extends Message {

    private Long userId;

    public InfoMessage(Long userId) {
        super(0L, Type.MSG_INFO);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
