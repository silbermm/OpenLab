package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.exceptions.*;
import edu.uc.labs.heartbeat.service.ClonezillaService;
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

    /*
    @RequestMapping(value = "restore", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String startCloning(@RequestBody CloneJson data) {
        try {
            service.startRestoreSession(data);
        } catch (SessionException e) {
            return e.getMessage();
        }
        return "OK";
    }
    */ 

    //@ExceptionHandler(SessionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleSessionException() {
    }

    @ExceptionHandler(ImageListingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleImageListingException() {
    }
    
    @Autowired ClonezillaService service;
}
