package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.dao.AuthorityDao;
import edu.uc.labs.heartbeat.dao.AuthorityDaoImpl;
import edu.uc.labs.heartbeat.dao.ImageDao;
import edu.uc.labs.heartbeat.dao.ImageDaoImpl;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.dao.MachineDaoImpl;
import edu.uc.labs.heartbeat.dao.MachineGroupDao;
import edu.uc.labs.heartbeat.dao.MachineGroupDaoImpl;
import edu.uc.labs.heartbeat.dao.RemoteImagingDao;
import edu.uc.labs.heartbeat.dao.RemoteImagingDaoImpl;
import edu.uc.labs.heartbeat.dao.MachineTaskDao;
import edu.uc.labs.heartbeat.dao.MachineTaskDaoImpl;
import edu.uc.labs.heartbeat.dao.WebUserDao;
import edu.uc.labs.heartbeat.dao.WebUserDaoImpl;
import edu.uc.labs.heartbeat.service.ClonezillaService;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
import edu.uc.labs.heartbeat.service.MachineTaskService;
import edu.uc.labs.heartbeat.service.WebUserService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.Async;

@Configuration
@Async
@EnableTransactionManagement
/*@ImportResource(value = {"/WEB-INF/spring-security.xml"})*/
@Import(PropertyPlaceholdersConfig.class)
public class WebappConfig {
    final private Logger log = LoggerFactory.getLogger(WebappConfig.class);
    @Bean
    public SessionFactory sessionFactory(){
        LocalSessionFactoryBuilder sf = new LocalSessionFactoryBuilder(dataSource).scanPackages("edu.uc.labs.heartbeat.models");
        sf.addProperties(getHibernateProperties());
        return sf.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager(){
        HibernateTransactionManager t = new HibernateTransactionManager();
        t.setSessionFactory(sessionFactory());
        return t;
    }

    @Bean
    public MachineDao machineDao() {
        MachineDao machineDao = new MachineDaoImpl(sessionFactory());
        return machineDao;
    }
    
    @Bean
    public ImageDao imageDao(){
        ImageDao imageDao = new ImageDaoImpl();
        return imageDao;
    }
    
    @Bean
    public RemoteImagingDao remoteImagingDao(){
        RemoteImagingDao remote = new RemoteImagingDaoImpl(sessionFactory());
        return remote;
    }

    @Bean
    public MachineGroupDao machineGroupDao(){
        MachineGroupDao machineGroupDao = new MachineGroupDaoImpl(sessionFactory());
        return machineGroupDao;
    }
    
    @Bean
    public WebUserDao webUserDao(){
        WebUserDao webUserDao = new WebUserDaoImpl(sessionFactory());
        return webUserDao;
    }
    
    @Bean
    public AuthorityDao authorityDao(){
        AuthorityDao authorityDao = new AuthorityDaoImpl(sessionFactory());
        return authorityDao;
    }
    
    @Bean
    public MachineTaskDao webTaskDao(){
        MachineTaskDao task = new MachineTaskDaoImpl(sessionFactory());
        return task;
    }

    @Bean
    public HeartbeatService heartbeatService(){
        HeartbeatService h = new HeartbeatService();
        h.setConfig(config);
        h.setMachineDao(machineDao());
        h.setMachineGroupDao(machineGroupDao());       
        return h;
    }
    
    @Bean
    public ClonezillaService clonezillaService(){
        return new ClonezillaService();
    }
    
    @Bean
    public RabbitService rabbitService(){
        return new RabbitService();
    }
    
    @Bean
    public MachineTaskService webTaskService(){
        return new MachineTaskService();
    }
    
    @Bean(name="webUserService")
    public WebUserService webUserService(){        
        return new WebUserService();
    }

    private Properties getHibernateProperties(){
        Properties p = new Properties();
        p.setProperty("hibernate.dialect", config.getString("hibernate.dialect"));
        p.setProperty("hibernate.show_sql", config.getString("hibernate.show_sql"));
        p.setProperty("hibernate.format_sql", config.getString("hibernate.format_sql"));
        p.setProperty("hibernate.hbm2ddl.auto", config.getString("hibernate.hbm2ddl.auto"));
        return p;
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    Config config;
    
    @Autowired
    AmqpTemplate heartbeatTemplate;

}
