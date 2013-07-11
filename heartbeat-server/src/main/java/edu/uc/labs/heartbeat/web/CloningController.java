package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.domain.CommandResult;
import edu.uc.labs.heartbeat.exceptions.*;
import edu.uc.labs.heartbeat.models.Failure;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.RemoteImageTask;
import edu.uc.labs.heartbeat.service.ClonezillaService;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
import edu.uc.labs.heartbeat.service.MachineTaskService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CloningController {
    
    @RequestMapping(value="/ipxe", method = RequestMethod.GET, produces="text/plain")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getIpxe(@RequestParam(value = "serial", required=false) String serial, @RequestParam(value="mac", required=false) String mac, @RequestParam(value="ip",required=false) String ip, @RequestParam(value="model",required=false) String model){                        
        ModelAndView m = new ModelAndView("jsp/ipxe");
        m.addObject("serial", serial);
        m.addObject("mac", mac);
        m.addObject("ip", ip);
        m.addObject("model", model);
        return m;
    }

    @RequestMapping(value = "/show/images", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String[] getImageNames() {
        return clonezillaService.getImages();
    }

    @RequestMapping(value = "/tasks/clone", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CommandResult startRemoteClone(@RequestBody RemoteImageTask task) {
        Machine m = heartbeatService.getMachineByUuid(task.getUuid());
        task.setCreated(new Date(System.currentTimeMillis()));
        if (rabbitService.checkAliveness(m)) {
            clonezillaService.setupRemoteImaging(task);
            CommandResult cr = rabbitService.startRemoteClone(m);
            if (cr.getExitCode() != 0) {
                throw new CommandFailedException(cr.getMessage());
            }
            return cr;
        } else {
            throw new MachineNotRespondingException("Machine not responing");
        }
    }

    @RequestMapping(value = "/tasks/reboot/{id}/to/{os}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CommandResult reboot(@PathVariable long id, @PathVariable String os) {
        log.info("Trying to reboot a machine");
        Machine m = heartbeatService.getMachineById(id);
        if (rabbitService.checkAliveness(m)) {
            CommandResult cr = rabbitService.reboot(m, os);
            if (cr.getExitCode() != 0) {
                throw new CommandFailedException(cr.getMessage());
            }
            return cr;
        } else {
            throw new MachineNotRespondingException("Machine not responding");
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleSessionException() {
    }

    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Failure handleGenericDataException(GenericDataException e) {
        return new Failure(e.getMessage());
    }

    @ExceptionHandler(MachineNotRespondingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Failure handleMachineNotRespondingException(MachineNotRespondingException e) {
        log.error(e.getMessage());
        return new Failure(e.getMessage());
    }

    @ExceptionHandler(ImageListingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    String handleImageListingException() {
        return "unable to find images";
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Failure handleException(IllegalStateException ex) {
        log.error("Exception --------->");
        return new Failure(ex.getMessage());
    }

    @ExceptionHandler(CommandFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Failure handleCommandFailedException(CommandFailedException ex) {
        return new Failure(ex.getMessage());
    }
    @Autowired
    HeartbeatService heartbeatService;
    @Autowired
    ClonezillaService clonezillaService;
    @Autowired
    RabbitService rabbitService;
    @Autowired
    MachineTaskService webTaskService;
    final private Logger log = LoggerFactory.getLogger(CloningController.class);
}
