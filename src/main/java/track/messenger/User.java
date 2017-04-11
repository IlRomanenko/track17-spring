package track.messenger;

import java.util.List;

/**
 * Tehnotrack
 * track.messenger
 * <p>
 * Created by ilya on 11.04.17.
 */
public class User {
    private long id;
    private String nickname;
    private List<Long> chatIds;

    public User(long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public List<Long> getChatIds() {
        return chatIds;
    }

    public void setChatIds(List<Long> chatIds) {
        this.chatIds = chatIds;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
