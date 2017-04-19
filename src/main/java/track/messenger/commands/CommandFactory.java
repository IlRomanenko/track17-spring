package track.messenger.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import track.messenger.messages.Type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tehnotrack
 * track.messenger.commands
 * <p>
 * Created by ilya on 11.04.17.
 */
@Repository
public class CommandFactory {

    private final Map<Type, Command> commandMap;

    @Autowired
    public CommandFactory(LoginCommand loginCommand, TextReceiveCommand textReceiveCommand, InfoCommand infoCommand,
                          UserCreateCommand userCreateCommand, UserListCommand userListCommand,
                          ChatCreateCommand chatCreateCommand, ChatHistoryCommand chatHistoryCommand,
                          ChatListCommand chatListCommand, TextSendCommand textSendCommand) {
        Map<Type, Command> tmpCommandMap = new HashMap<>();
        tmpCommandMap.put(Type.MSG_LOGIN, loginCommand);
        tmpCommandMap.put(Type.MSG_INFO, infoCommand);

        tmpCommandMap.put(Type.MSG_CREATE_USER, userCreateCommand);
        tmpCommandMap.put(Type.MSG_USERS_LIST, userListCommand);

        tmpCommandMap.put(Type.MSG_CHAT_CREATE, chatCreateCommand);
        tmpCommandMap.put(Type.MSG_CHAT_HIST, chatHistoryCommand);
        tmpCommandMap.put(Type.MSG_CHAT_LIST, chatListCommand);

        tmpCommandMap.put(Type.MSG_TEXT, textReceiveCommand);

        tmpCommandMap.put(Type.MSG_SEND_TEXT, textSendCommand);

        commandMap = Collections.unmodifiableMap(tmpCommandMap);
        instance = this;
    }

    public Command create(Type type) {
        return commandMap.getOrDefault(type, null);
    }

    private static CommandFactory instance;

    public static CommandFactory getInstance() {
        return instance;
    }
}
