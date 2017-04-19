package track.messenger.messages.requests;

import track.messenger.messages.BaseTextMessage;

/**
 * Tehnotrack
 * track.messenger.messages.requests
 * <p>
 * Created by ilya on 19.04.17.
 */
public class ChatMessage extends BaseTextMessage {

    private long chatId;

    public ChatMessage(Long senderId, Long chatId, String text) {
        super(senderId, text);
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
