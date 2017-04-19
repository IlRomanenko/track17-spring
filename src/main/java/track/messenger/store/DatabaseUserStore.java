package track.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import track.messenger.models.User;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Tehnotrack
 * track.messenger.store
 * <p>
 * Created by ilya on 13.04.17.
 */
@Repository
public class DatabaseUserStore implements UserStore {

    private static Logger log = LoggerFactory.getLogger(DatabaseUserStore.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Connection connection;

    private PreparedStatement addUserStatement;
    private PreparedStatement updateUserStatement;
    private PreparedStatement getUserStatement;
    private PreparedStatement getUserByIdStatement;
    private PreparedStatement getAllUsers;

    @PostConstruct
    public void postConstruct() {
        initSchema();
        prepareStatements();
    }

    private void initSchema() {
        log.info("Init user schema");
        String createTableQuery =
                "CREATE TABLE IF NOT EXISTS Messenger.Users (" +
                        "id INTEGER NOT NULL AUTO_INCREMENT," +
                        "login VARCHAR UNIQUE NOT NULL," +
                        "password VARCHAR NOT NULL)";

        jdbcTemplate.execute(createTableQuery);
        log.info("User schema has initialized");
    }

    private void prepareStatements() {
        try {
            log.info("Prepare statements");
            addUserStatement = connection.prepareStatement("INSERT INTO Messenger.Users(login, password)" +
                    " VALUES (?, ?)", new String[]{"id"});


            updateUserStatement = connection.prepareStatement("UPDATE Messenger.Users SET login = ? , " +
                    "password = ? WHERE id = ?");

            getUserStatement = connection.prepareStatement("SELECT * FROM Messenger.Users WHERE login = ?" +
                    " AND password = ?");

            getUserByIdStatement = connection.prepareStatement("SELECT * FROM Messenger.Users WHERE id = ?");

            getAllUsers = connection.prepareStatement("SELECT id, login FROM Messenger.Users");

            log.info("Statements has prepared");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public User addUser(String login, String password) {
        User user = null;
        try {
            log.info("Add user");
            addUserStatement.setString(1, login);
            addUserStatement.setString(2, password);
            addUserStatement.execute();
            ResultSet resultSet = addUserStatement.getGeneratedKeys();

            while (resultSet.next()) {
                user = new User(resultSet.getLong(1), login, null);
            }

            log.info(String.format("User has added {0}", user));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return user;
    }

    @Override
    public User updateUser(User user) {
        try {
            log.info("Update user");
            updateUserStatement.setString(1, user.getNickname());
            updateUserStatement.setString(2, user.getPassword());
            updateUserStatement.setLong(3, user.getId());
            user = getUserById(user.getId());
            log.info("User has updated");
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            user = null;
        }
        return user;
    }

    @Override
    public User getUser(String login, String password) {
        User user = null;
        try {
            log.info("Get user by login-password");
            getUserStatement.setString(1, login);
            getUserStatement.setString(2, password);

            ResultSet result = getUserStatement.executeQuery();
            if (result.next()) {
                user = new User(result.getLong("id"), result.getString("login"),
                        result.getString("password"));
                log.info("User has found");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        try {
            log.info("Get user by id");
            getUserByIdStatement.setLong(1, id);

            ResultSet result = getUserByIdStatement.executeQuery();
            if (result.next()) {
                user = new User(result.getLong("id"), result.getString("login"),
                        result.getString("password"));
            }
            log.info("User has found");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> result = null;
        try {
            log.info("Get all users");

            ResultSet resultSet = getAllUsers.executeQuery();

            result = new LinkedList<>();

            while (resultSet.next()) {
                result.add(new User(resultSet.getLong("id"), resultSet.getString("login"), null));
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        return result;
    }
}
