package edu.uc.labs.heartbeat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.domain.Command;
import edu.uc.labs.heartbeat.domain.CommandResult;
import edu.uc.labs.heartbeat.exceptions.MachineNotRespondingException;
import edu.uc.labs.heartbeat.exceptions.RabbitFetchException;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.utils.HeartbeatUtil;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
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
        if (m.getOs().startsWith("Win")) {
            cmd.setCmd("cmd.exe");
            cmd.addArg("/c");
            cmd.addArg("setBoot.pl");
            cmd.addArg("-r");
            cmd.addArg("Leg");
            cmd.addArg("1");
        } else {
            cmd.setCmd("setBoot.pl");
            cmd.addArg("-r");
            cmd.addArg("Leg");
            cmd.addArg("1");
        }
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
        if (m.getOs().startsWith("Win")) {
            cmd.setCmd("cmd.exe");
            cmd.addArg("/c");
            cmd.addArg("setBoot.pl");
            cmd.addArg("-r");
            cmd.addArg(os);
            cmd.addArg("1");
        } else {
            cmd.setCmd("setBoot.pl");
            cmd.addArg("-r");
            cmd.addArg(os);
            cmd.addArg("1");
        }
        CommandResult response = (CommandResult) heartbeatTemplate.convertSendAndReceive("machine.cmd", m.getUid() + "-cmd", cmd, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message msg) throws AmqpException {
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            }
        });
        return response;
    }

    public List<LinkedHashMap> getAllQueues() {
        //HttpGet httpget = new HttpGet("http://iris.uc.edu:15672/api/queues/%2fheartbeat");
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<LinkedHashMap> listObject = mapper.readValue(httpClient.execute(httpget, responseHandler), List.class);
            return listObject;
        } catch (RuntimeException e) {
            throw new RabbitFetchException(e.getMessage());
        } catch (Exception e) {
            throw new RabbitFetchException(e.getMessage());
        }
    }

    public void cleanupMachineQueue() {

        try {
            List<LinkedHashMap> allQueues = getAllQueues();
            for (LinkedHashMap m : allQueues) {
                String qName = (String) m.get("name");
                log.info("looking at : " + qName);
                if (!qName.endsWith("cmd")) {
                    ClientMachine c = new ClientMachine();
                    c.setUuid(qName);
                    try {
                        ClientMachine response = (ClientMachine) heartbeatTemplate.convertSendAndReceive("machine.status", qName, c);
                        if (!response.getUuid().equals(c.getUuid())) {
                            // delete the queue and the associated cmd queue
                            log.info("trying to delete queue " + qName + " and " + qName + "-cmd");
                            amqpAdmin.deleteQueue(qName);
                            amqpAdmin.deleteQueue(qName + "-cmd");
                        }
                    } catch (Exception e) {
                        log.info("no answer on queue...");
                        log.info("trying to delete queue " + qName + " and " + qName + "-cmd");
                        amqpAdmin.deleteQueue(qName);
                        amqpAdmin.deleteQueue(qName + "-cmd");
                    }
                }
            }
        } catch (RabbitFetchException r) {
            log.error(r.getMessage());
        } catch (Exception e) {
            log.error(e.getCause());
            log.error(e.getMessage());
        }

    }
    private static final Logger log = Logger.getLogger(RabbitService.class);
    @Autowired
    private AmqpTemplate heartbeatTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    private Config config;
    @Autowired
    HttpClient httpClient;
    @Autowired
    HttpGet httpget;
}
