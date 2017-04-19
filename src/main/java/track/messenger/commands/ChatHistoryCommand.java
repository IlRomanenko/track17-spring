package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.ChatHistoryMessage;
import track.messenger.messages.responses.ChatHistoryResultMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.net.Session;
import track.messenger.store.MessageStore;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class ChatHistoryCommand implements Command {

    @Autowired
    private MessageStore messageStore;

    @Override
    public void execute(Session session, Message message) {

        ChatHistoryMessage msg = (ChatHistoryMessage) message;

        List<Long> messagesIds = messageStore.getMessagesFromChat(msg.getChatId());

        if (messagesIds == null) {
            session.send(new StatusMessage(StatusMessage.Status.NOT_FOUND));
        } else {
            session.send(new ChatHistoryResultMessage(messagesIds));
        }

    }
}
