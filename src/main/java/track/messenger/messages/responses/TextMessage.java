package track.messenger.messages.responses;

import track.messenger.messages.BaseTextMessage;
import track.messenger.messages.Type;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class TextMessage extends BaseTextMessage{

    private long id;
    private long chatId;

    public TextMessage(long id, Long senderId, Long chatId, String text) {
        super(senderId, text);
        setType(Type.MSG_SEND_TEXT);
        this.id = id;
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        TextMessage message = (TextMessage) other;
        return Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}