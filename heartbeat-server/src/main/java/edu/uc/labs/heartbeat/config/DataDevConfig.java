package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile(value = "dev")
@Configuration
public class DataDevConfig implements DataConfig {

    @Override
    @Bean(destroyMethod="close")
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(config.getString("db.dev.driverClass"));
        basicDataSource.setUrl(config.getString("db.dev.url"));
        basicDataSource.setUsername(config.getString("db.dev.username"));
        basicDataSource.setPassword(config.getString("db.dev.password"));
        basicDataSource.setMaxWait(config.getLong("db.dev.maxwait"));
        return basicDataSource;
    }

    @Autowired
    Config config;

}
