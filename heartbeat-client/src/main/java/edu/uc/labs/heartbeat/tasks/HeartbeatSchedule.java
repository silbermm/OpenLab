package edu.uc.labs.heartbeat.tasks;


import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import org.apache.log4j.Logger;

public class HeartbeatSchedule implements Scheduler {

    public HeartbeatSchedule(Config config, HeartbeatService heartbeatService) {
        this.config = config;
        this.heartbeatService = heartbeatService;
    }

    @Override
    public void run() {
        log.debug("running HeartbeatScheduler -- " + hasRun);
        if (!hasRun) {
            // this is the first time being run
            // or the computer has been restarted...
            log.debug("running heartbeat for the first time (or after machine restart)");
            m = heartbeatService.getMachineInfo();
            heartbeatService.writeMachineInfo(m);
            heartbeatService.sendDataToServer(m, true);
            hasRun = true;
        } else {
            // look for the current user...
            log.debug("heartbeat is just trying to get the current user");
            if (!heartbeatService.getCurrentUser().equals(m.getCurrentUser())) {
                m.setCurrentUser(heartbeatService.getCurrentUser());
                heartbeatService.sendDataToServer(m, true);
            } else heartbeatService.sendDataToServer(m, false);
        }
    }


    private static final Logger log = Logger.getLogger(HeartbeatSchedule.class);
    private Config config;
    private HeartbeatService heartbeatService;
    private boolean hasRun = false;
    private ClientMachine m;

}
