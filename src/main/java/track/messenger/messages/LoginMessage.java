package track.messenger.messages;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 11.04.17.
 */
public class LoginMessage extends Message{

    private String login;
    private String password;

    public LoginMessage(Long senderId, String login, String password) {
        super(senderId, Type.MSG_LOGIN);
        this.login = login;
        this.password = password;
    }


}
