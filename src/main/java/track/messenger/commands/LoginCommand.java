package track.messenger.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.LoginMessage;
import track.messenger.messages.responses.InfoResultMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.models.User;
import track.messenger.net.Session;
import track.messenger.net.SessionException;
import track.messenger.store.UserStore;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class LoginCommand implements Command {

    private static Logger log = LoggerFactory.getLogger(LoginCommand.class);

    @Autowired
    private UserStore userStore;

    @Override
    public void execute(Session session, Message message) {
        log.info("Execute login message : " + message.toString());


        LoginMessage loginMessage = (LoginMessage)message;

        User user = userStore.getUser(loginMessage.getLogin(), loginMessage.getPassword());

        if (user == null) {
            session.send(new StatusMessage(StatusMessage.Status.NOT_FOUND));
            return;
        }
        try {
            session.setUser(user);
            session.getHandler().authSession(session.getId(), user);
            session.send(new InfoResultMessage(user));

        } catch (SessionException ex) {
            log.error(ex.getMessage());
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
        }
    }
}
