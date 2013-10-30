package co.silbersoft.openlab.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import co.silbersoft.openlab.config.AppContext;
import co.silbersoft.openlab.config.RabbitConfig;
import co.silbersoft.openlab.config.WebappConfig;
import co.silbersoft.openlab.dao.LdapDaoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebappConfig.class, AppContext.class, RabbitConfig.class})
public class TestLdapSearch {
		
	
	
	@Autowired LdapDaoImpl ldapDao;
	@Autowired AuthenticationManager authenticationManager;
}
