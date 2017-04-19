package track.messenger.messages.requests;

import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class UserCreateMessage extends LoginMessage {

    public UserCreateMessage(String login, String password) {
        super(login, password);
        setType(Type.MSG_CREATE_USER);
    }
}
