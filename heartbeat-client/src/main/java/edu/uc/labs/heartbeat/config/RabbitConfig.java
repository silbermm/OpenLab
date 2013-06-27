package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.*;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.tasks.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.log4j.Logger;

@Configuration
public class RabbitConfig {

    private static final Logger log = Logger.getLogger(RabbitConfig.class);

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
    public Queue machineQueue() {
        Queue q = new Queue(heartbeatService.getUUID());
        return q;
    }

    @Bean
    public Queue commandQueue() {
        Queue q = new Queue(heartbeatService.getUUID() + "-cmd");
        log.debug("Creating queue named " + heartbeatService.getUUID() + "-cmd");
        return q;
    }

    @Bean
    public DirectExchange heartbeatExchange() {
        DirectExchange ex = new DirectExchange("machine.status", true, false);
        log.debug("Creating queue named " + heartbeatService.getUUID());
        return ex;
    }

    @Bean
    public DirectExchange commandExchange() {
        DirectExchange ex = new DirectExchange("machine.cmd", true, false);
        return ex;
    }

    @Bean
    public Binding heartbeatBinding() {
        return BindingBuilder.bind(
                machineQueue()).to(heartbeatExchange()).with(heartbeatService.getUUID());
    }

    @Bean
    public Binding commandBinding() {
        return BindingBuilder.bind(
                commandQueue()).to(commandExchange()).with(heartbeatService.getUUID() + "-cmd");
    }

    @Bean
    public AmqpAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.declareQueue(machineQueue());
        admin.declareQueue(commandQueue());
        admin.declareExchange(heartbeatExchange());
        admin.declareExchange(commandExchange());
        admin.declareBinding(heartbeatBinding());
        admin.declareBinding(commandBinding());
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public AmqpTemplate heartbeatTemplate() {
        RabbitTemplate r = new RabbitTemplate(connectionFactory());
        r.setMessageConverter(heartbeatMessageConverter());
        return r;
    }

    @Bean
    public AmqpTemplate commandTemplate() {
        RabbitTemplate r = new RabbitTemplate(connectionFactory());
        r.setMessageConverter(commandResultConverter());
        return r;
    }

    @Bean
    public JsonMessageConverter commandMsgConverter() {
        JsonMessageConverter converter = new JsonMessageConverter();
        DefaultClassMapper mapper = new DefaultClassMapper();
        mapper.setDefaultType(Command.class);
        converter.setClassMapper(mapper);
        return converter;
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
    public JsonMessageConverter commandResultConverter(){
        JsonMessageConverter converter = new JsonMessageConverter();
        DefaultClassMapper mapper = new DefaultClassMapper();
        mapper.setDefaultType(CommandResult.class);
        converter.setClassMapper(mapper);
        return converter;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(machineQueue());
        container.setMessageListener(new HeartbeatMessageListener(heartbeatService, heartbeatTemplate()));
        container.setAutoStartup(true);
        //container.setMessageConverter(heartbeatMessageConverter());
        container.setConcurrentConsumers(1);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer commandListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(commandQueue());
        container.setMessageListener(new CommandMsgListener(heartbeatService, commandTemplate(), commandMsgConverter()));
        container.setAutoStartup(true);
        //container.setMessageConverter(commandMsgConverter());
        container.setConcurrentConsumers(1);
        return container;
    }
    @Autowired
    Config config;
    @Autowired
    HeartbeatService heartbeatService;
}
