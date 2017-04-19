package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.ChatCreateMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.models.Chat;
import track.messenger.net.Session;
import track.messenger.store.MessageStore;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Service
public class ChatCreateCommand implements Command {

    @Autowired
    private MessageStore messageStore;

    @Override
    public void execute(Session session, Message message) {

        ChatCreateMessage msg = (ChatCreateMessage) message;

        Chat chat = messageStore.createChat(msg.getSenderId(), msg.getUsers(), msg.getChatName());

        if (chat == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
        } else {
            session.send(new StatusMessage(StatusMessage.Status.OK));
        }
    }
}
