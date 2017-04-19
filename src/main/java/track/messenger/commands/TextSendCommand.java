package track.messenger.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.responses.TextMessage;
import track.messenger.models.Chat;
import track.messenger.models.User;
import track.messenger.net.Session;
import track.messenger.store.MessageStore;

import java.util.Map;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 19.04.17.
 */
@Service
public class TextSendCommand implements Command {

    private Logger log = LoggerFactory.getLogger(TextSendCommand.class);


    @Autowired
    private MessageStore messageStore;

    @Override
    public void execute(Session session, Message message) {
        TextMessage msg = (TextMessage) message;

        Chat chat = messageStore.getChatById(msg.getChatId());

        if (chat == null) {
            log.error("Sending message to unknown chat");
            return;
        }

        Map<User, Session> authSessions = session.getHandler().getAuthSessions();

        for (User user : chat.getUsers()) {
            Session userSession = authSessions.getOrDefault(user, null);
            if (userSession == null) {
                continue;
            }
            if (user.getId() == session.getUser().getId()) {
                continue;
            }
            userSession.send(msg);
        }

    }
}
