package co.silbersoft.openlab.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import co.silbersoft.openlab.dao.LdapDao;
import co.silbersoft.openlab.dao.LdapDaoImpl;

import com.typesafe.config.Config;


@Configuration
public class LdapConfig {
	
    @Bean
    public LdapContextSource ldapContext() {
        Map<String, String> props = new HashMap<String, String>();
        props.put("java.naming.security.authentication", "simple");
        LdapContextSource ldapContext = new LdapContextSource();
        //List<String> urls = config.getStringList("ldap.url");
        //ldapContext.setUrls((String[]) urls.toArray(new String[0]));
        ldapContext.setUrl(config.getString("ldap.url"));
        ldapContext.setBase(config.getString("ldap.baseSearch"));
        ldapContext.setUserDn(config.getString("ldap.utilitydn"));
        ldapContext.setPassword(config.getString("ldap.utilitypass"));
        ldapContext.setReferral("follow");
        ldapContext.setBaseEnvironmentProperties(props);
        return ldapContext;
    }

    @Bean
    public LdapTemplate ldapTemplate(){
        return new LdapTemplate(ldapContext());
    }
	
    @Bean
    public LdapDao ldapDao(){
    	return new LdapDaoImpl();
    }
       
	@Autowired Config config;

}
