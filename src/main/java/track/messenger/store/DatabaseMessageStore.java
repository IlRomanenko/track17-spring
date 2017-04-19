package track.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import track.messenger.messages.BaseTextMessage;
import track.messenger.messages.Message;
import track.messenger.messages.responses.TextMessage;
import track.messenger.models.Chat;
import track.messenger.models.User;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Tehnotrack
 * track.messenger.store
 * <p>
 * Created by ilya on 14.04.17.
 */
@Repository
public class DatabaseMessageStore implements MessageStore {

    private Logger log = LoggerFactory.getLogger(DatabaseMessageStore.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Connection connection;

    @Autowired
    private UserStore userStore;

    private PreparedStatement getChatsByUserIdStatement;
    private PreparedStatement getChatByIdStatement;
    private PreparedStatement getMessagesFromChatStatement;
    private PreparedStatement getMessageByIdStatement;
    private PreparedStatement addMessageStatement;
    private PreparedStatement addUserToChatStatement;
    private PreparedStatement getUsersInChatStatement;
    private PreparedStatement createChatStatement;


    @PostConstruct
    public void postConstruct() {
        initSchema();
        prepareStatements();
    }


    private void initSchema() {
        log.info("Init message schema");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS Messenger.Messages (" +
                        "id INTEGER NOT NULL AUTO_INCREMENT," +
                        "sender_id INTEGER NOT NULL," +
                        "chat_id INTEGER NOT NULL," +
                        "text VARCHAR NOT NULL)");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS Messenger.Chats (" +
                        "id INTEGER NOT NULL AUTO_INCREMENT," +
                        "title VARCHAR NOT NULL," +
                        "admin_id INTEGER NOT NULL)");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS Messenger.UsersChats (" +
                        "chat_id INTEGER NOT NULL," +
                        "user_id INTEGER NOT NULL)");

        log.info("Message schema has initialized");

    }

    private void prepareStatements() {
        try {
            log.info("Prepare message statements");

            getChatsByUserIdStatement = connection.prepareStatement(
                    "SELECT id, title FROM Messenger.Chats " +
                            "INNER JOIN Messenger.UsersChats ON Messenger.Chats.id = Messenger.UsersChats.chat_id " +
                            "WHERE Messenger.UsersChats.user_id = ?");

            getChatByIdStatement = connection.prepareStatement(
                    "SELECT id, title, admin_id " +
                            "FROM Messenger.Chats WHERE Messenger.Chats.id = ?");

            getUsersInChatStatement = connection.prepareStatement(
                    "SELECT user_id FROM Messenger.UsersChats WHERE chat_id = ?");

            getMessageByIdStatement = connection.prepareStatement(
                    "SELECT id, sender_id, text " +
                            "FROM Messenger.Messages " +
                            "WHERE Messenger.Messages.chat_id = ?");

            addMessageStatement = connection.prepareStatement(
                    "INSERT INTO Messenger.Messages(SENDER_ID, TEXT, CHAT_ID) VALUES (?, ?, ?)",
                    new String[]{"id"});

            addUserToChatStatement = connection.prepareStatement(
                    "INSERT INTO MESSENGER.USERSCHATS(CHAT_ID, USER_ID) VALUES (?, ?)",
                    new String[]{"id"});

            createChatStatement = connection.prepareStatement(
                    "INSERT INTO Messenger.Chats(TITLE, ADMIN_ID) VALUES (?, ?)",
                    new String[]{"id"});

            getMessagesFromChatStatement = connection.prepareStatement(
                    "SELECT id FROM MESSENGER.Messages WHERE Messenger.Messages.CHAT_ID = ?");

            log.info("Message statements have initialized");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public List<Map.Entry<Long, String>> getChatsByUserId(Long userId) {
        List<Map.Entry<Long, String>> chats = new LinkedList<>();
        try {
            log.info("Get user by id");
            getChatsByUserIdStatement.setLong(1, userId);

            ResultSet result = getChatsByUserIdStatement.executeQuery();
            while (result.next()) {
                chats.add(new AbstractMap.SimpleEntry<>(result.getLong("id"),
                        result.getString("title")));
            }
            log.info("User has found");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return chats;
    }

    @Override
    public Chat getChatById(Long chatId) {
        Chat chat = null;

        try {
            getChatByIdStatement.setLong(1, chatId);
            ResultSet result = getChatByIdStatement.executeQuery();
            if (result.next()) {

                getUsersInChatStatement.setLong(1, chatId);
                ResultSet resultUsersSet = getUsersInChatStatement.executeQuery();
                List<User> users = new LinkedList<>();
                while (resultUsersSet.next()) {
                    users.add(userStore.getUserById(resultUsersSet.getLong("user_id")));
                }

                long adminId = result.getLong("admin_id");
                String title = result.getString("title");
                User admin = userStore.getUserById(adminId);

                chat = new Chat(chatId, title, admin, users);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return chat;
    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) {
        List<Long> resultList = null;
        try {

            getMessagesFromChatStatement.setLong(1, chatId);

            ResultSet result = getMessagesFromChatStatement.executeQuery();

            resultList = new LinkedList<>();

            while (result.next()) {
                resultList.add(result.getLong("id"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return resultList;
    }

    @Override
    public Message getMessageById(Long messageId) {
        Message msg = null;
        try {

            getMessageByIdStatement.setLong(1, messageId);

            ResultSet result = getMessageByIdStatement.executeQuery();

            if (result.next()) {
                msg = new TextMessage(
                        messageId,
                        result.getLong("sender_id"),
                        result.getLong("chat_id"),
                        result.getString("text"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return msg;
    }

    @Override
    public TextMessage addMessage(Long chatId, BaseTextMessage message) {
        TextMessage msg = null;

        try {

            addMessageStatement.setLong(1, message.getSenderId());
            addMessageStatement.setString(2, message.getText());
            addMessageStatement.setLong(3, chatId);

            addMessageStatement.execute();

            ResultSet resultSet = addMessageStatement.getGeneratedKeys();

            if (resultSet.next()) {
                msg = new TextMessage(
                        resultSet.getLong(1),
                        message.getSenderId(),
                        chatId,
                        message.getText());
            }

        } catch (SQLException e) {
            log.error(e.getMessage());

        }

        return msg;
    }

    @Override
    public boolean addUserToChat(Long userId, Long chatId) {
        boolean ok = true;

        try {
            addUserToChatStatement.setLong(1, chatId);
            addUserToChatStatement.setLong(2, userId);

            addUserToChatStatement.execute();
            ResultSet resultSet = addUserToChatStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                ok = false;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            ok = false;
        }

        return ok;
    }

    @Override
    public Chat createChat(Long adminId, List<Long> usersIds, String title) {
        Chat chat = null;

        boolean ok = true;

        try {

            createChatStatement.setString(1, title);
            createChatStatement.setLong(2, adminId);

            createChatStatement.execute();
            ResultSet resultSet = addMessageStatement.getGeneratedKeys();

            Long chatId = -1L;
            if (resultSet.next()) {
                chatId = resultSet.getLong(1);
            } else {
                return null;
            }

            List<User> users = new LinkedList<>();
            for (Long userId : usersIds) {
                addUserToChat(userId, chatId);

                User user = userStore.getUserById(userId);
                if (user == null) {
                    ok = false;
                }
                users.add(user);
            }
            addUserToChat(adminId, chatId);

            User admin = userStore.getUserById(adminId);
            if (admin == null) {
                ok = false;
            }

            if (ok) {
                chat = new Chat(chatId, title, admin, users);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return chat;
    }
}
