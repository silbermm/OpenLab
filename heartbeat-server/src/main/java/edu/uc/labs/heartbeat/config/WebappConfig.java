package edu.uc.labs.heartbeat.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.uc.labs.heartbeat.dao.ImageDao;
import edu.uc.labs.heartbeat.dao.ImageDaoImpl;
import edu.uc.labs.heartbeat.service.AccountService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@Async
@EnableTransactionManagement
@ImportResource(value = {"/WEB-INF/spring-security.xml"})
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
    public MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource msg = new ReloadableResourceBundleMessageSource();
        msg.setBasename("/WEB-INF/messages/messages");
        msg.setCacheSeconds(0);
        return msg;
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder sf = new LocalSessionFactoryBuilder(dataSource()).scanPackages("edu.uc.labs.heartbeat.models");
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
    
    @Bean(destroyMethod="close")
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(config().getString("db.dev.driverClass"));
        basicDataSource.setUrl(config().getString("db.dev.url"));
        basicDataSource.setUsername(config().getString("db.dev.username"));
        basicDataSource.setPassword(config().getString("db.dev.password"));
        basicDataSource.setMaxWait(config().getLong("db.dev.maxwait"));
        return basicDataSource;
    }

    private Properties getHibernateProperties() {
        Properties p = new Properties();
        p.setProperty("hibernate.dialect", config().getString("hibernate.dialect"));
        p.setProperty("hibernate.show_sql", config().getString("hibernate.show_sql"));
        p.setProperty("hibernate.format_sql", config().getString("hibernate.format_sql"));
        p.setProperty("hibernate.hbm2ddl.auto", config().getString("hibernate.hbm2ddl.auto"));
        return p;
    }

    @Autowired
    AmqpTemplate heartbeatTemplate;
}
