package edu.uc.labs.heartbeat.test;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import co.silbersoft.openlab.config.*;
import co.silbersoft.openlab.exceptions.RabbitFetchException;
import co.silbersoft.openlab.service.RabbitService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebappConfig.class, AppContext.class, RabbitConfig.class})
public class TestRabbitCleanup {

    @Test
    public void testGetQueues(){
        try {
            List<LinkedHashMap> list = rabbitService.getAllQueues();
            log.info("list size = " + list.size());
            
            log.info(list.get(0).get("name"));
            
           
            if(list.isEmpty()){
                Assert.fail();
            } else {
                Assert.assertTrue(true);
            }
        } catch (RabbitFetchException ex){
            log.error(ex.getMessage());
            Assert.fail();
        }
        
        
        
    }
    
    @Test
    public void testCleanupQueues(){
        try {
            rabbitService.cleanupMachineQueue();            
        } catch(RuntimeException e){
            log.error(e.getMessage());
            Assert.fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }                
    }
    
    
    
    private static final Logger log = Logger.getLogger(TestRabbitCleanup.class);
    @Autowired RabbitService rabbitService;
    
}
