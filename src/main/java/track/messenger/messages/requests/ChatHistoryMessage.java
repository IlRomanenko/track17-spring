package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class ChatHistoryMessage extends Message {

    private Long chatId;

    public ChatHistoryMessage(Long senderId, Long chatId) {
        super(senderId, Type.MSG_CHAT_HIST);
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
