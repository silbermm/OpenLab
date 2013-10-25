package co.silbersoft.openlab.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import co.silbersoft.openlab.config.AppContext;
import co.silbersoft.openlab.config.RabbitConfig;
import co.silbersoft.openlab.config.WebappConfig;
import co.silbersoft.openlab.dao.LdapDaoImpl;
import co.silbersoft.openlab.models.LdapUser;
import co.silbersoft.openlab.service.authentication.ActiveDirectoryUserDetailsService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebappConfig.class, AppContext.class, RabbitConfig.class})
public class TestLdapSearch {
	
	
	
	@Before
	public void setupAuth(){
		UserDetails userDetails = adUserDetailsService.loadUserByUsername("silbermm");
		Authentication authToken = new UsernamePasswordAuthenticationToken (userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	
	@Test
	public void testLdapSearch(){
		List<LdapUser> uList = ldapDao.findByUsername("silbermm");
		for(LdapUser u: uList){
			System.out.println(u);
		}
		Assert.notEmpty(uList);
	}
	
	@Autowired LdapDaoImpl ldapDao;
	ActiveDirectoryUserDetailsService adUserDetailsService = new ActiveDirectoryUserDetailsService();
}
