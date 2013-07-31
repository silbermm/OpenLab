package edu.uc.labs.heartbeat.config;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class PropertyPlaceholdersConfig {

    @Bean
    public Config config(){
        return ConfigFactory.load().withFallback(ConfigFactory.systemProperties());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();

    }
}
