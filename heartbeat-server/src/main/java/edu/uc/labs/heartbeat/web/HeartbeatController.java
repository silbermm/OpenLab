package edu.uc.labs.heartbeat.web;



import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.MachineGroup;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "/")
public class HeartbeatController {

    final static Logger log = LoggerFactory.getLogger(HeartbeatController.class);

    @RequestMapping(value="create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void acceptMachineInfo(@RequestBody ClientMachine clientMachine, HttpServletRequest request){
        log.info("in the acceptMachineInfo method");
        boolean success = heartbeatService.updateMachine(clientMachine, request.getRemoteAddr());
        log.info("Updated " + success);
    }

    /*@RequestMapping(value="create/group", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createGroup(@RequestBody MachineGroup machineGroup){

    }*/

    @RequestMapping(value="show/machines", method = RequestMethod.GET)
    public @ResponseBody List<Machine> getAllMachines(){
        return heartbeatService.getAllMachines();
    }

    @RequestMapping(value="show/machine/uuid/{uuid}", method = RequestMethod.GET)
    public @ResponseBody
    Machine getMachineByUuid(@PathVariable String uuid){
        return heartbeatService.getMachineByUuid(uuid);
    }

    @RequestMapping(value="show/machine/id/{id}", method = RequestMethod.GET)
    public @ResponseBody Machine getMachineById(@PathVariable Long id){
        return heartbeatService.getMachineById(id);
    }

    @RequestMapping(value = "show/groups", method = RequestMethod.GET)
    public @ResponseBody List<MachineGroup> getGroups(){
        return heartbeatService.getAllMachineGroups();
    }
    
    @RequestMapping(value = "show/group/id/{id}", method = RequestMethod.GET)
    public @ResponseBody MachineGroup getGroupById(@PathVariable Long id){
        return heartbeatService.getGroupById(id);
    }


    @RequestMapping(value="update/machine/uuid/{uuid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void acceptUuid(@PathVariable String uuid){
        heartbeatService.updateMachineRecord(uuid);
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public String homePage(Model model){
        return "index";
    }
    
    @RequestMapping(value="machine/{uuid}", method= RequestMethod.GET)
    public String computerPage(@PathVariable String uuid, Model model){
        model.addAttribute("machineUUID", uuid);
        return "computer";
    }



    @Autowired
    HeartbeatService heartbeatService;

}
