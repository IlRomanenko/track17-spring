package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class ChatCreateMessage extends Message {

    private String chatName;
    private List<Long> users;

    public ChatCreateMessage(Long senderId, String chatName, List<Long> users) {
        super(senderId, Type.MSG_CHAT_CREATE);
        this.chatName = chatName;
        this.users = users;
    }

    public String getChatName() {
        return chatName;
    }

    public List<Long> getUsers() {
        return users;
    }
}
