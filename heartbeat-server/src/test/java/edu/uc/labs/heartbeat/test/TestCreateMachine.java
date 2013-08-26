package edu.uc.labs.heartbeat.test;

import edu.uc.labs.heartbeat.config.*;
import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PropertyPlaceholdersConfig.class, WebappConfig.class, RabbitConfig.class, DataDevConfig.class})
@ActiveProfiles("dev")
public class TestCreateMachine {
    
    ClientMachine m = new ClientMachine();
    Machine dbM;
    
    @Before
    public void createMachine(){
        m.setCurrentUser("silbermm");
        m.setFacility("UCLABS");        
        m.setMac("00:00:00:00:00");
        m.setManufacturer("Apple");
        m.setModel("iMac");
        m.setName("TEstMachine");
        m.setOs("Mac OS X");
        m.setOsVersion("10.8");
        m.setUuid("09876509-0909-0909-0909-09909090");        
        m.setSerialNumber("8989837834");
    }
    
    @Test
    public void testCreateMachine(){                
        heartbeatService.updateMachine(m, "10.12.12.12");                                       
        dbM = heartbeatService.getMachineByUuid("09876509-0909-0909-0909-09909090");
        Assert.assertEquals(dbM.getMac(), m.getMac());
    }
    
    @After    
    public void deleteMachine(){
       heartbeatService.deleteMachine(dbM);
    }
    
    

    @Autowired HeartbeatService heartbeatService;   
}
