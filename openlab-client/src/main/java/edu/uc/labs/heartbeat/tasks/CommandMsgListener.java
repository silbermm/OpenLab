package edu.uc.labs.heartbeat.tasks;

import com.rabbitmq.client.Channel;
import edu.uc.labs.heartbeat.service.*;
import edu.uc.labs.heartbeat.domain.*;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

public class CommandMsgListener implements ChannelAwareMessageListener {

    public CommandMsgListener(HeartbeatService heartbeatService, AmqpTemplate template, MessageConverter converter) {
        this.heartbeatService = heartbeatService;
        this.template = template;
        this.converter = converter;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        final MessageProperties props = message.getMessageProperties();        
        // try to run the given command or script                     
        Command c = (Command) converter.fromMessage(message);
        CommandResult result = heartbeatService.runCommand(c);      
        template.convertAndSend(props.getReplyTo(), result, new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message msg) throws AmqpException {
                msg.getMessageProperties().setCorrelationId(props.getCorrelationId());
                return msg;
            }            
        });
    }
    
    private MessageConverter converter;
    private HeartbeatService heartbeatService;
    private AmqpTemplate template;
}
