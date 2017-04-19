package track.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Tehnotrack
 * track.messenger.store
 * <p>
 * Created by ilya on 13.04.17.
 */
@Repository
public class MessengerDAO {
    private static Logger log = LoggerFactory.getLogger(MessengerDAO.class);

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private Connection connection;

    private static final String SCHEMA_NAME = "Messenger";

    @PostConstruct
    public void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        initSchema();
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }
    }

    private void initSchema() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA_NAME);
    }

    @Bean
    public JdbcTemplate getJdbc() {
        return jdbcTemplate;
    }

    @Bean
    public Connection getConnection() {
        return connection;
    }

}
