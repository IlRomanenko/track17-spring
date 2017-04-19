package track.messenger.models;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tehnotrack
 * track.messenger
 * <p>
 * Created by ilya on 11.04.17.
 */
public class Chat implements Serializable {

    private Long id;
    private User admin;
    private List<User> users;
    private String title;

    public Chat(Long id, String title, User admin, List<User> users) {
        this.id = id;
        this.title = title;
        this.admin = admin;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public User getAdmin() {
        return admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getTitle() {
        return title;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        String ids = users.stream().map(user -> Long.toString(user.getId())).collect(Collectors.joining(" : "));
        return String.format("Chat{(id : %d) (title : %s) (User : %s) (Users : < %s >)}", id, title, admin.toString(), ids);
    }
}
