package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.dao.ImageDao;
import edu.uc.labs.heartbeat.dao.ImageDaoImpl;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.dao.MachineDaoImpl;
import edu.uc.labs.heartbeat.dao.MachineGroupDao;
import edu.uc.labs.heartbeat.dao.MachineGroupDaoImpl;
import edu.uc.labs.heartbeat.dao.RemoteImagingDao;
import edu.uc.labs.heartbeat.dao.RemoteImagingDaoImpl;
import edu.uc.labs.heartbeat.service.ClonezillaService;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
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

@Configuration
@EnableTransactionManagement
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
