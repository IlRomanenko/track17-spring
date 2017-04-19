package track.messenger.store;

import track.messenger.messages.BaseTextMessage;
import track.messenger.messages.Message;
import track.messenger.messages.responses.TextMessage;
import track.messenger.models.Chat;

import java.util.List;
import java.util.Map;

public interface MessageStore {

    List<Map.Entry<Long, String>> getChatsByUserId(Long userId);

    /**
     * получить информацию о чате
     */
    Chat getChatById(Long chatId);

    /**
     * Список сообщений из чата
     */
    List<Long> getMessagesFromChat(Long chatId);

    /**
     * Получить информацию о сообщении
     */
    Message getMessageById(Long messageId);

    /**
     * Добавить сообщение в чат
     */
    TextMessage addMessage(Long chatId, BaseTextMessage message);

    /**
     * Добавить пользователя к чату
     */
    boolean addUserToChat(Long userId, Long chatId);

    Chat createChat(Long adminId, List<Long> usersIds, String title);
}
