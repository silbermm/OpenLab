package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.dao.MachineDaoImpl;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.tasks.HeartbeatSchedule;
import edu.uc.labs.heartbeat.tasks.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@Async
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addFixedDelayTask(
                new Runnable() {
                    public void run() {
                        heartbeatScheduler().run();
                    }
                },
                config().getLong("heartbeat.interval")
        );
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public Config config() {
        return ConfigFactory.load().withFallback(ConfigFactory.systemProperties());
    }

    @Bean
    public ResourceBundleMessageSource resourceBundle() {
        ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
        resource.setBasename("locale/CliBundle");
        return resource;
    }

    @Bean
    public MachineDao machineDao() {
        return new MachineDaoImpl(config(), restTemplate());
    }

    @Bean
    public HeartbeatService heartbeatService() {
        HeartbeatService heartbeatService = new HeartbeatService();
        heartbeatService.setMachineDao(machineDao());
        heartbeatService.setConfig(config());
        heartbeatService.setMessages(resourceBundle());
        return heartbeatService;
    }

    @Bean
    public Scheduler heartbeatScheduler() {
        return new HeartbeatSchedule(config(), heartbeatService());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
