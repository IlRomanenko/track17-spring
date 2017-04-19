package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track.messenger.messages.Message;
import track.messenger.messages.requests.ChatMessage;
import track.messenger.messages.responses.StatusMessage;
import track.messenger.messages.responses.TextMessage;
import track.messenger.net.Session;
import track.messenger.store.MessageStore;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 14.04.17.
 */
@Service
public class TextReceiveCommand implements Command {

    @Autowired
    private MessageStore messageStore;

    @Override
    public void execute(Session session, Message message) {
        ChatMessage msg = (ChatMessage)message;

        TextMessage storedMessage = messageStore.addMessage(msg.getChatId(), msg);

        if (storedMessage == null) {
            session.send(new StatusMessage(StatusMessage.Status.FAIL));
        } else {
            session.send(new StatusMessage(StatusMessage.Status.OK));

            session.getHandler().sendMessageInChat(session, storedMessage);
        }
    }
}
