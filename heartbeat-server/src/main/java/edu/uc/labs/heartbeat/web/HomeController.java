package edu.uc.labs.heartbeat.web;



import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/")
public class HomeController {

    final static Logger log = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value="", method = RequestMethod.POST, headers ={"Accept=application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void acceptMachineInfo(@RequestBody Machine machine){
        log.info("in the acceptMachineInfo method");
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public @ResponseBody List<Machine> getAllMachines(){
        return heartbeatService.getAllMachines();
    }


    @Autowired
    HeartbeatService heartbeatService;

}
