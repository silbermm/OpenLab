package co.silbersoft.openlab.controllers;

import edu.uc.labs.heartbeat.domain.CommandResult;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import co.silbersoft.openlab.exceptions.*;
import co.silbersoft.openlab.models.Failure;
import co.silbersoft.openlab.models.Machine;
import co.silbersoft.openlab.models.RemoteImageTask;
import co.silbersoft.openlab.service.ClonezillaService;
import co.silbersoft.openlab.service.HeartbeatService;
import co.silbersoft.openlab.service.MachineTaskService;
import co.silbersoft.openlab.service.RabbitService;

@Controller
public class CloningController {

    /**
     * This method returns an ipxe script that the ipxe client will use to print
     * the menu or automatically start the clone
     *
     * @param serial
     * @param mac
     * @param ip
     * @param model
     * @return
     */
    @RequestMapping(value = "/ipxe", method = RequestMethod.GET, produces = "text/plain")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getIpxe(@RequestParam(value = "serial", required = false) String serial, @RequestParam(value = "mac", required = false) String mac, @RequestParam(value = "ip", required = false) String ip, @RequestParam(value = "model", required = false) String model) {
        RemoteImageTask rt = clonezillaService.isTaskPending(serial, mac, ip);
        if (rt != null) {
            // there is a pending task...
            ModelAndView m = new ModelAndView("jsp/ipxe");
            m.addObject("serial", serial);
            m.addObject("mac", mac);
            m.addObject("ip", ip);
            m.addObject("model", model);
            m.addObject("image", rt.getImageName());
            return m;
        } else {
            ModelAndView m = new ModelAndView("jsp/ipxe-menu");
            m.addObject("images", clonezillaService.getImages());
            return m;
        }

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

    @ExceptionHandler(ImageTaskException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView handleImageTaskException(ImageTaskException e) {
        log.error("Got an error while trying to clone a machine... ");
        if (e.getRemoteImageTask() != null) {
            log.error("machine I was trying to clone was: " + e.getRemoteImageTask().getUuid());
        }
        // TODO: should we just try to reboot into an OS?
        ModelAndView m = new ModelAndView("jsp/ipxe-menu");
        m.addObject("images", clonezillaService.getImages());
        return m;
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
