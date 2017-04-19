package track.messenger.messages.responses;

import track.messenger.messages.Type;
import track.messenger.models.User;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 13.04.17.
 */
public class InfoResultMessage extends StatusMessage{

    private User user;

    public InfoResultMessage(User user) {
        super(Status.OK, Type.MSG_INFO_RESULT);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
