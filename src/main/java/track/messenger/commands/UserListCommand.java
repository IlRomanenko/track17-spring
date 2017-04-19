package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.responses.UserListResultMessage;
import track.messenger.models.User;
import track.messenger.net.Session;
import track.messenger.store.UserStore;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class UserListCommand implements Command {

    @Autowired
    private UserStore userStore;

    @Override
    public void execute(Session session, Message message) {
        List<User> users = userStore.getUsers();
        session.send(new UserListResultMessage(users));
    }
}
