package co.silbersoft.openlab.config;

import co.silbersoft.openlab.dao.ImageDao;
import co.silbersoft.openlab.dao.ImageDaoImpl;
import co.silbersoft.openlab.service.AccountService;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "co.silbersoft.openlab" })
public class AppContext {

	private static final Logger log = LoggerFactory.getLogger(AppContext.class);

	@Bean
	public Config config() {
		return ConfigFactory.load().withFallback(
				ConfigFactory.systemProperties());
	}

	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder sf = new LocalSessionFactoryBuilder(
				dataSource()).scanPackages("co.silbersoft.openlab.models");
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

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(config().getString("db.dev.driverClass"));
		basicDataSource.setUrl(config().getString("db.dev.url"));
		basicDataSource.setUsername(config().getString("db.dev.username"));
		basicDataSource.setPassword(config().getString("db.dev.password"));
		basicDataSource.setMaxWait(config().getLong("db.dev.maxwait"));
		basicDataSource.addConnectionProperty("autoReconnect", "true");
		basicDataSource.setMaxActive(-1);	    
		return basicDataSource;
	}
	
	

	private Properties getHibernateProperties() {
		Properties p = new Properties();
		p.setProperty("hibernate.dialect",
				config().getString("hibernate.dialect"));
		p.setProperty("hibernate.show_sql",
				config().getString("hibernate.show_sql"));
		p.setProperty("hibernate.format_sql",
				config().getString("hibernate.format_sql"));
		p.setProperty("hibernate.hbm2ddl.auto",
				config().getString("hibernate.hbm2ddl.auto"));
		return p;
	}

	@Autowired AmqpTemplate heartbeatTemplate;
}
