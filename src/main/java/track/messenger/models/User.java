package track.messenger.models;

import java.io.Serializable;
import java.util.List;

/**
 * Tehnotrack
 * track.messenger
 * <p>
 * Created by ilya on 11.04.17.
 */
public class User implements Serializable {

    private long id;
    private String nickname;
    private String password;

    private List<Long> chatIds;

    public User(long id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
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

    @Override
    public int hashCode() {
        return ((Long)id).intValue();
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return ((User)other).id == id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User{id: %d login: %s password %s}", id, nickname, password);
    }
}
