package track.messenger.messages.responses;

import track.messenger.messages.Type;
import track.messenger.models.User;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class UserListResultMessage extends StatusMessage {

    private List<User> users;

    public UserListResultMessage(List<User> users) {
        super(Status.OK, Type.MSG_USERS_LIST_RESULT);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
