package track.messenger.commands;

import track.messenger.messages.Message;
import track.messenger.net.Session;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
public interface Command {

    void execute(Session session, Message message);
}
