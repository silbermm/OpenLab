package co.silbersoft.openlab.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import co.silbersoft.openlab.service.HeartbeatService;
import co.silbersoft.openlab.service.RabbitService;

public class RabbitCleanupTask implements ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(RabbitCleanupTask.class);

    @Async
    @Override
    public void run() {
        rabbitCleanupThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                rabbitService.cleanupMachineQueue();
            }
        });
    }
    @Autowired
    ThreadPoolTaskExecutor rabbitCleanupThreadPool;
    
    @Autowired
    HeartbeatService heartbeatService;
    
    @Autowired
    RabbitService rabbitService;
   
}
