package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.domain.CommandResult;
import edu.uc.labs.heartbeat.exceptions.*;
import edu.uc.labs.heartbeat.models.Failure;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.RemoteImageTask;
import edu.uc.labs.heartbeat.service.ClonezillaService;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
import java.util.Date;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/")
public class CloningController {

    @RequestMapping(value = "/show/images", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String[] getImageNames() {
        return service.getImages();
    }
    
    @RequestMapping(value="/tasks/clone", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CommandResult startRemoteClone(@RequestBody RemoteImageTask task) {    
        Machine m = heartbeatService.getMachineByUuid(task.getUuid());
        task.setCreated(new Date(System.currentTimeMillis()));        
        if(rabbitService.checkAliveness(m)){
            log.info("machine with uuid: " + task.getUuid() + " appears to be alive and responding...");
            service.setupRemoteImaging(task);        
            return rabbitService.startRemoteClone(m);
        } else {
            throw new MachineNotRespondingException("Machine not responing");
        }                
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleSessionException() {
    }

    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleGenericDataException(GenericDataException e) {
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(MachineNotRespondingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleMachineNotRespondingException(MachineNotRespondingException e){
        log.error(e.getMessage());
        return new Failure(e.getMessage());
    }

    @ExceptionHandler(ImageListingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    String handleImageListingException() {
        return "unable to find images";
    }
    
    @Autowired HeartbeatService heartbeatService;
    @Autowired ClonezillaService service;
    @Autowired RabbitService rabbitService;
    
    final private Logger log = LoggerFactory.getLogger(CloningController.class);
}
