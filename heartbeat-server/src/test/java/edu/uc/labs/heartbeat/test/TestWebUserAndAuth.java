/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uc.labs.heartbeat.test;


import edu.uc.labs.heartbeat.config.RabbitConfig;
import edu.uc.labs.heartbeat.config.WebappConfig;
import edu.uc.labs.heartbeat.models.WebUser;
import edu.uc.labs.heartbeat.service.AccountService;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebappConfig.class, RabbitConfig.class})
public class TestWebUserAndAuth {
 
    private static final Logger log = Logger.getLogger(TestWebUserAndAuth.class);
    
    @Test
    public void testUserJoin(){
        
        List<WebUser> users = webUserService.getWebUsersWith(1);
        log.debug(users.size() + " Users with that role");
        for(WebUser u : users){
            log.debug(u.getCn());
        }
        
        Assert.notNull(users);
        
    }
    
    
    @Autowired AccountService webUserService;
    
}
