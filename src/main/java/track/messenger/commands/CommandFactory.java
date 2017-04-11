package track.messenger.commands;

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
public class CommandFactory {


    private static Map<Type, Command> commandMap;

    static {
        Map<Type, Command> tmpCommandMap = new HashMap<>();


        tmpCommandMap.put(Type.MSG_LOGIN, new LoginCommand());
        tmpCommandMap.put(Type.MSG_INFO, new InfoCommand());

        tmpCommandMap.put(Type.MSG_CREATE_USER, new UserCreateCommand());
        tmpCommandMap.put(Type.MSG_USERS_LIST, new UserListCommand());

        tmpCommandMap.put(Type.MSG_CHAT_CREATE, new ChatCreateCommand());
        tmpCommandMap.put(Type.MSG_CHAT_HIST, new ChatHistoryCommand());
        tmpCommandMap.put(Type.MSG_CHAT_LIST, new ChatListCommand());

        commandMap = Collections.unmodifiableMap(tmpCommandMap);
    }

    public static Command create(Type type) {
        return commandMap.getOrDefault(type, null);
    }
}
