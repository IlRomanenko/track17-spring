package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.InfoMessage;
import track.messenger.messages.responses.InfoResultMessage;
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
public class InfoCommand implements Command {

    @Autowired
    private UserStore userStore;

    @Override
    public void execute(Session session, Message message) {
        InfoMessage msg = (InfoMessage)message;

        User user = userStore.getUserById(msg.getUserId());

        if (user == null) {
            session.send(new StatusMessage(StatusMessage.Status.NOT_FOUND));
        } else {
            session.send(new InfoResultMessage(user));
        }

    }
}
