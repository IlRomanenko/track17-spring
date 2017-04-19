package track.messenger.messages.responses;

import track.messenger.messages.Type;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class ChatHistoryResultMessage extends StatusMessage {

    private List<Long> messagesId;

    public ChatHistoryResultMessage(List<Long> messagesId) {
        super(Status.OK, Type.MSG_CHAT_HIST_RESULT);
        this.messagesId = messagesId;
    }

    public List<Long> getMessagesId() {
        return messagesId;
    }
}
