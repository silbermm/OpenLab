package edu.uc.labs.heartbeat.tasks;

import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class RabbitCleanupTask implements ScheduledTask {
           
    @Async
    @Override
    public void run() {
        rabbitCleanupThreadPool.execute(new Runnable(){
            @Override
            public void run() {
                
            }        
        });
    }
    
    @Autowired ThreadPoolTaskExecutor rabbitCleanupThreadPool;
    @Autowired HeartbeatService heartbeatService;
    @Autowired RabbitService rabbitService;
    
}
