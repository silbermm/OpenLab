package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.domain.Machine;
import org.apache.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatService {

    public Machine getMachineInfo() {
        Machine m = machineDao.getMachineInfo();
        machineDao.writeToFile(m);
        return m;
    }

    public void writeMachineInfo(Machine m) {
        machineDao.writeToFile(m);
    }

    /**
     * Send the data to the server
     *
     * @param m
     * @param changes if there were changes ( i.e. username ) send the entire object, other wise we just want to update the time
     */
    public void sendDataToServer(Machine m, boolean changes) {
        if (changes) {
            machineDao.sendToServer(m);
        } else {
            machineDao.sendToServer(m.getUuid());
        }
    }

    public String getCurrentUser() {
        return machineDao.findLoggedInUser();
    }

    public String getUUID() {
        return machineDao.findUUID();
    }

    public String getComputerName() {
        return machineDao.findComputerName();
    }

    public void setMachineDao(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setMessages(ResourceBundleMessageSource messages) {
        this.messages = messages;
    }

    private static final Logger log = Logger.getLogger(HeartbeatService.class);
    private MachineDao machineDao;
    private ResourceBundleMessageSource messages;
    private Config config;

}
