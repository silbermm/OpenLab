package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.utils.HeartbeatUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {
    
    public RabbitService(Config config){
        this.config = config;
    }
    
    public boolean checkAliveness(Machine m){
        ClientMachine cm = HeartbeatUtil.convertMachineToClientMachine(m);       
        ClientMachine response = (ClientMachine) heartbeatTemplate.convertSendAndReceive(config.getString("heartbeat.exchange.status"), cm.getUuid(), cm);
        if(response.getUuid().equals(cm.getUuid())){
            return true;
        }
        return false;
    }
    
    
    
    public void setHeartbeatTemplate(AmqpTemplate heartbeatTemplate){
        this.heartbeatTemplate = heartbeatTemplate;
    }
    
    private static final Logger log = Logger.getLogger(RabbitService.class);
    private AmqpTemplate heartbeatTemplate;
    private Config config;
}
