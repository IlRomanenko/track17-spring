package track.messenger.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.UserCreateMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.models.User;
import track.messenger.net.Session;
import track.messenger.store.UserStore;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class UserCreateCommand implements Command {

    private static Logger log = LoggerFactory.getLogger(UserCreateCommand.class);

    @Autowired
    private UserStore userStore;

    @Override
    public void execute(Session session, Message message) {
        UserCreateMessage msg = (UserCreateMessage) message;
        if (msg == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
            return;
        }
        User user = userStore.addUser(msg.getLogin(), msg.getPassword());

        if (user == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
        } else {
            session.send(new StatusMessage(StatusMessage.Status.OK));
        }
    }
}
