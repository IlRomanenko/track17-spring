package track.messenger.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.messages.Message;
import track.messenger.net.Session;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
public class LoginCommand implements Command {

    private static Logger LOG = LoggerFactory.getLogger(LoginCommand.class);

    @Override
    public void execute(Session session, Message message) {
        LOG.info("Execute : ", message.toString());
    }
}
