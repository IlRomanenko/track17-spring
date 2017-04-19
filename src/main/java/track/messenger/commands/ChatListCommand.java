package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.ChatListMessage;
import track.messenger.messages.responses.ChatListResultMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.net.Session;
import track.messenger.store.MessageStore;

import java.util.List;
import java.util.Map;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class ChatListCommand implements Command {

    @Autowired
    private MessageStore messageStore;

    @Override
    public void execute(Session session, Message message) {
        ChatListMessage msg = (ChatListMessage)message;

        if (session.getUser() == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
            return;
        }

        List<Map.Entry<Long, String>> list = messageStore.getChatsByUserId(session.getUser().getId());

        if (list == null) {
            session.send(new StatusMessage(StatusMessage.Status.NOT_FOUND));
        } else {
            session.send(new ChatListResultMessage(list));
        }

    }
}
