package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.client.RestTemplate;

/**
 *
 * @author silbermm
 */
@Configuration
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(config.getString("rabbit.host"));
        connectionFactory.setVirtualHost(config.getString("rabbit.vhost"));
        connectionFactory.setUsername(config.getString("rabbit.username"));
        connectionFactory.setPassword(config.getString("rabbit.password"));
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        return admin;
    }

    @Bean
    public JsonMessageConverter heartbeatMessageConverter() {
        JsonMessageConverter converter = new JsonMessageConverter();
        DefaultClassMapper mapper = new DefaultClassMapper();
        mapper.setDefaultType(ClientMachine.class);
        converter.setClassMapper(mapper);
        return converter;
    }

    @Bean
    public AmqpTemplate heartbeatTemplate() {
        RabbitTemplate r = new RabbitTemplate(connectionFactory());
        r.setMessageConverter(heartbeatMessageConverter());
        return r;
    }

    @Bean
    public HttpClient rabbitHttpClient() {                        
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(config.getString("rabbit.host"), 15672),
                new UsernamePasswordCredentials(config.getString("rabbit.username"), config.getString("rabbit.password")));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();                                
        return httpclient;        
    }
    
    @Autowired
    Config config;
}
