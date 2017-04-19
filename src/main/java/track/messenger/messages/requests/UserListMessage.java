package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class UserListMessage extends Message {

    public UserListMessage() {
        super(0L, Type.MSG_USERS_LIST);
    }
}
