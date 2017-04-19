package track.messenger.store;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Tehnotrack
 * track.messenger.store
 * <p>
 * Created by ilya on 13.04.17.
 */
@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource getDataSource(
            @Value("${track.messenger.store.jdbc:}") String jdbcUrl,
            @Value("${track.messenger.store.username:}") String username,
            @Value("${track.messenger.store.password:}") String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(org.h2.Driver.class.getName());
        config.setJdbcUrl("jdbc:h2:" + jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
