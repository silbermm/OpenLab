package co.silbersoft.openlab.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.models.Failure;
import co.silbersoft.openlab.models.Machine;
import co.silbersoft.openlab.models.MachineGroup;
import co.silbersoft.openlab.service.HeartbeatService;
import edu.uc.labs.heartbeat.domain.ClientMachine;

@Controller
@RequestMapping(value = "/")
public class HeartbeatController {

    final static Logger log = LoggerFactory.getLogger(HeartbeatController.class);
    
    
    @RequestMapping(value = "login", method = RequestMethod.GET)    
    public String login(@RequestParam(value = "error", required = false) String error, ModelMap model){
        if(error != null && error.equals("authFailed")){
            model.addAttribute("error", "true");
        }
        return "login";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value="create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createComputer(@RequestBody ClientMachine clientMachine, HttpServletRequest request){
        boolean success = heartbeatService.updateMachine(clientMachine, request.getRemoteAddr());
    }
    
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "machine/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void acceptMachineInfo(@RequestBody ClientMachine clientMachine, HttpServletRequest request) {
        boolean success = heartbeatService.updateMachine(clientMachine, request.getRemoteAddr());
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value="update/machine/uuid/{uuid}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateMachine(@PathVariable String uuid, HttpServletRequest request){        
        heartbeatService.updateMachineRecord(uuid);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value = "group", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<String, String> createGroup(@RequestBody MachineGroup machineGroup) {
        long created = heartbeatService.createGroup(machineGroup);
        Map cr = new HashMap();
        cr.put("created", created);
        return cr;
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission(#machineGroup.groupId, 'isGroupAdmin')")
    @RequestMapping(value="group", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@RequestBody MachineGroup machineGroup) {
        heartbeatService.deleteGroup(machineGroup);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value = "group", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateGroup(@RequestBody MachineGroup machineGroup){
        heartbeatService.updateGroup(machineGroup);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "machine/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Machine> getAllMachines() {
        return heartbeatService.getAllMachines();
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "machine/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteMachineById(@PathVariable Long id) {
        log.info("trying to delete machine " + id);
        Machine m = heartbeatService.getMachineById(id);
        heartbeatService.deleteMachine(m);
    }
    
    @PreAuthorize(value = "")
    @RequestMapping(value = "machine/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Machine getMachineByUuid(@PathVariable Long id) {
        return heartbeatService.getMachineById(id);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "group/all", method = RequestMethod.GET)
    public @ResponseBody
    List<MachineGroup> getGroups() {
        return heartbeatService.getAllMachineGroups();
    }

    @RequestMapping(value = "group/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MachineGroup getGroupById(@PathVariable Long id) {
        return heartbeatService.getGroupById(id);
    }
    
    @RequestMapping(value = "group/{id}/machines", method = RequestMethod.GET)
    public @ResponseBody Set<Machine> getMachinesInGroup(@PathVariable Long id) {
        return heartbeatService.getMachinesInGroup(id);
    }
    

    @RequestMapping(value = "group/{groupId}/machine", method = RequestMethod.DELETE)
    public void deleteMachineInGroup(@PathVariable Long groupId, @RequestParam(value = "id", required = true) Long machineId) {
        Machine m = heartbeatService.getMachineById(machineId);
        heartbeatService.deleteMachine(m);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "machine/{uuid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void acceptUuid(@PathVariable String uuid) {
        heartbeatService.updateMachineRecord(uuid);
    }

    @RequestMapping(value = "machine/{uuid}/to/{groupId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void changeMachineGroup(@PathVariable String uuid, @PathVariable long groupId) {
        heartbeatService.moveMachine(uuid, groupId);
        log.info("Moved machine " + uuid + " to " + groupId);
    }
    
    @RequestMapping(value = "machine/id/{id}/to/{groupId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void moveMachine(@PathVariable long id, @PathVariable long groupId){
        heartbeatService.moveMachine(id, groupId);
        log.info("Moved machine " + id + " to " + groupId);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "show/default-os/for/{serialNumber}", method = RequestMethod.GET)
    public @ResponseBody
    String getDefaultOs(@PathVariable String serialNumber) {
        Machine m = heartbeatService.getMachineBySerial(serialNumber);
        return m.getDefaultOs();
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String homePage(Model model) {
        return "index";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "check/auth", method = RequestMethod.GET)
    public @ResponseBody
    List<String> checkAuthentication(HttpServletRequest req, Principal principal) {
        List<String> auths = new ArrayList<String>(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
        for (GrantedAuthority g : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            auths.add(g.getAuthority());
        }
        return auths;
    }        
    
    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Failure handleGenericDataException(GenericDataException e) {
        return new Failure(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    Failure handleAuthFaiureFailure(AccessDeniedException ae) {
        return new Failure("Access Denied");
    }
    
    @ExceptionHandler(CookieTheftException.class)
    public String handleCookieTheft(Model m){        
        m.addAttribute("error", "Its possible someone stole your cookie");
        return "redirect:login";
    }    
    
    @Autowired
    HeartbeatService heartbeatService;
}
