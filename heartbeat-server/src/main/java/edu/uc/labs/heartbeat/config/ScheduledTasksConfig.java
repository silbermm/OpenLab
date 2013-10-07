package edu.uc.labs.heartbeat.config;

import edu.uc.labs.heartbeat.tasks.RabbitCleanupTask;
import edu.uc.labs.heartbeat.tasks.ScheduledTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableAsync
@EnableScheduling
public class ScheduledTasksConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addFixedDelayTask(new Runnable() {
            public void run() {
                rabbitCleanup().run();
            }
        },10000);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean(name="rabbitCleanupThreadPool")
    public ThreadPoolTaskExecutor rabbitCleanupThreadPool() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(2);
        t.setMaxPoolSize(3);
        t.setQueueCapacity(3);
        t.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return t;
    }
    
    @Bean
    public ScheduledTask rabbitCleanup(){
        return new RabbitCleanupTask();
    }
}
