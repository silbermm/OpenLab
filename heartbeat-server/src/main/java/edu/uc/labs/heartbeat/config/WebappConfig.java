package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import edu.uc.labs.heartbeat.service.AccountService;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@Async
@EnableTransactionManagement
@ImportResource(value = {"/WEB-INF/spring-security.xml"})
@Import(PropertyPlaceholdersConfig.class)
@ComponentScan(basePackages = {"edu.uc.labs.heartbeat"})
public class WebappConfig extends WebMvcConfigurerAdapter {

    final private Logger log = LoggerFactory.getLogger(WebappConfig.class);

    @Bean
    public Config config() {
        return ConfigFactory.load().withFallback(ConfigFactory.systemProperties());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ipxe").setViewName("ipxe.jsp");
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(null);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder sf = new LocalSessionFactoryBuilder(dataSource).scanPackages("edu.uc.labs.heartbeat.models");
        sf.addProperties(getHibernateProperties());
        return sf.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager t = new HibernateTransactionManager();
        t.setSessionFactory(sessionFactory());
        return t;
    }

    @Bean
    public ImageDao imageDao() {
        ImageDao imageDao = new ImageDaoImpl();
        return imageDao;
    }

    @Bean(name = "webUserService")
    public AccountService webUserService() {
        return new AccountService();
    }

    private Properties getHibernateProperties() {
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
