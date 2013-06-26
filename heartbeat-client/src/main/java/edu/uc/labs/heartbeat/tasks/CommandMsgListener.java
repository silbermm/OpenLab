package edu.uc.labs.heartbeat.tasks;

import com.rabbitmq.client.Channel;
import edu.uc.labs.heartbeat.service.*;
import edu.uc.labs.heartbeat.domain.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

public class CommandMsgListener implements ChannelAwareMessageListener {

    public CommandMsgListener(HeartbeatService heartbeatService, AmqpTemplate template, MessageConverter converter) {
        this.heartbeatService = heartbeatService;
        this.template = template;
        this.converter = converter;
    }

    public void onMessage(Message message, Channel channel) throws Exception {
        final MessageProperties props = message.getMessageProperties();
        // try to run the given command or script                
        Command c = (Command) converter.fromMessage(message);
        String result = heartbeatService.runCommand(c);

        MessageProperties newProps = new MessageProperties();
        newProps.setCorrelationId(props.getCorrelationId());
        Message m = new Message(result.getBytes(), newProps);
        template.send(props.getReplyTo(), m);
    }
    private MessageConverter converter;
    private HeartbeatService heartbeatService;
    private AmqpTemplate template;
}
