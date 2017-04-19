package track.messenger.messages.responses;

import track.messenger.messages.Type;

import java.util.List;
import java.util.Map;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class ChatListResultMessage extends StatusMessage {

    private List<Map.Entry<Long, String>> chats;

    public ChatListResultMessage(List<Map.Entry<Long, String>> chats) {
        super(Status.OK, Type.MSG_CHAT_LIST_RESULT);
        this.chats = chats;

    }

    public List<Map.Entry<Long, String>> getChats() {
        return chats;
    }

}
