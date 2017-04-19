package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 15.04.17.
 */
public class ChatListMessage extends Message {

    public ChatListMessage(Long senderId) {
        super(senderId, Type.MSG_CHAT_LIST);
    }
}
