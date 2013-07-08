package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.domain.Command;
import edu.uc.labs.heartbeat.domain.CommandResult;
import edu.uc.labs.heartbeat.exceptions.MachineNotRespondingException;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.utils.HeartbeatUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    public boolean checkAliveness(Machine m) {
        try {
            ClientMachine cm = HeartbeatUtil.convertMachineToClientMachine(m);
            ClientMachine response = (ClientMachine) heartbeatTemplate.convertSendAndReceive("machine.status", cm.getUuid(), cm);
            if (response.getUuid().equals(cm.getUuid())) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            log.error("******************************* " + e.getMessage() + " *******************************************");
            throw new MachineNotRespondingException(e.getMessage());
        } catch (Exception e) {
            log.error("******************************* " + e.getMessage() + " *******************************************");
            throw new MachineNotRespondingException(e.getMessage());
        }
    }

    public CommandResult startRemoteClone(Machine m) {
        Command cmd = new Command();
        cmd.setCmd("/Volumes/zilla/setBoot.pl");
        cmd.addArg("-r");
        cmd.addArg("Leg");
        cmd.addArg("1");
        //cmd.setCmd("say");
        //cmd.addArg("hello");
        CommandResult response = (CommandResult) heartbeatTemplate.convertSendAndReceive("machine.cmd", m.getUid() + "-cmd", cmd, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message msg) throws AmqpException {
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            }
        });
        return response;
    }

    public CommandResult reboot(Machine m, String os) {
        Command cmd = new Command();
        
        
        
        CommandResult response = (CommandResult) heartbeatTemplate.convertSendAndReceive("machine.cmd", m.getUid() + "-cmd", cmd, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message msg) throws AmqpException {
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            }
        });
        return response;
    }
    private static final Logger log = Logger.getLogger(RabbitService.class);
    @Autowired
    private AmqpTemplate heartbeatTemplate;
    @Autowired
    private Config config;
}
