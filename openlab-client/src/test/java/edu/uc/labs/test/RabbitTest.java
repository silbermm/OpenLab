package edu.uc.labs.test;

import edu.uc.labs.heartbeat.config.AppConfig;
import edu.uc.labs.heartbeat.config.RabbitConfig;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, RabbitConfig.class})
@ActiveProfiles("dev")
public class RabbitTest {

    @Test
    public void testHeartbeatQ() {

        ClientMachine cm = heartbeatService.getMachineInfo();
        ClientMachine response = (ClientMachine) heartbeatTemplate.convertSendAndReceive("machine.status", cm.getUuid(), cm, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message msg) throws AmqpException {
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            }
        });

        System.out.println("************************ " + response + " *******************************");
        Assert.assertEquals(cm.getUuid(), response.getUuid());
    }   
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private HeartbeatService heartbeatService;
    
    @Autowired
    private AmqpTemplate heartbeatTemplate;
    
}
