package track.messenger.messages.requests;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 11.04.17.
 */
public class LoginMessage extends Message {

    private String login;
    private String password;

    public LoginMessage(String login, String password) {
        super(0L, Type.MSG_LOGIN);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
