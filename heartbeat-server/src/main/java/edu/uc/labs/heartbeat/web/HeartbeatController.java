package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.domain.ClientMachine;
import edu.uc.labs.heartbeat.exceptions.GenericDataException;
import edu.uc.labs.heartbeat.models.Failure;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.MachineGroup;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import java.security.Principal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
@RequestMapping(value = "/")
public class HeartbeatController {
        
    final static Logger log = LoggerFactory.getLogger(HeartbeatController.class);

    @PreAuthorize("permitAll()")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void acceptMachineInfo(@RequestBody ClientMachine clientMachine, HttpServletRequest request){       
        boolean success = heartbeatService.updateMachine(clientMachine, request.getRemoteAddr());        
    }

    /*@RequestMapping(value="create/group", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createGroup(@RequestBody MachineGroup machineGroup){
    }*/

    @PreAuthorize("permitAll()")
    @RequestMapping(value="show/machines", method = RequestMethod.GET)
    public @ResponseBody List<Machine> getAllMachines(){
        return heartbeatService.getAllMachines();
    }

    @PreAuthorize(value = "")
    @RequestMapping(value="show/machine/uuid/{uuid}", method = RequestMethod.GET)
    public @ResponseBody
    Machine getMachineByUuid(@PathVariable String uuid){
        return heartbeatService.getMachineByUuid(uuid);
    }

    @RequestMapping(value="show/machine/id/{id}", method = RequestMethod.GET)
    public @ResponseBody Machine getMachineById(@PathVariable Long id){
        return heartbeatService.getMachineById(id);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "show/groups", method = RequestMethod.GET)
    public @ResponseBody List<MachineGroup> getGroups(){
        return heartbeatService.getAllMachineGroups();
    }        
    
    @RequestMapping(value = "show/group/id/{id}", method = RequestMethod.GET)
    public @ResponseBody MachineGroup getGroupById(@PathVariable Long id){
        return heartbeatService.getGroupById(id);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value="update/machine/uuid/{uuid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void acceptUuid(@PathVariable String uuid){
        heartbeatService.updateMachineRecord(uuid);
    }
    
    @RequestMapping(value="move/machine/{uuid}/to/{groupId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void changeMachineGroup(@PathVariable String uuid, @PathVariable long groupId){
        heartbeatService.moveMachine(uuid, groupId);
        log.info("Moved machine " + uuid + " to " + groupId);
    }
    
    @PreAuthorize("permitAll()")
    @RequestMapping(value="show/default-os/for/{serialNumber}", method = RequestMethod.GET)
    public @ResponseBody String getDefaultOs(@PathVariable String serialNumber){        
        Machine m = heartbeatService.getMachineBySerial(serialNumber);
        return m.getDefaultOs();
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value="", method = RequestMethod.GET)
    public String homePage(Model model){
        return "jsp/index";
    }
    
    @PreAuthorize("permitAll()")
    @RequestMapping(value="check/auth", method = RequestMethod.GET)
    public @ResponseBody List<String> checkAuthentication(HttpServletRequest req, Principal principal){
        List<String> auths = new ArrayList<String>(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
        for(GrantedAuthority g : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            auths.add(g.getAuthority());
        }
        return auths;
    }
    
    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleGenericDataException(GenericDataException e){
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Failure handleAuthFaiureFailure(AccessDeniedException ae) {
    	return new Failure("Access Denied");
    }
   
    @Autowired HeartbeatService heartbeatService;   
}
