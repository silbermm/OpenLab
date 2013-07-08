package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.exceptions.GenericDataException;
import edu.uc.labs.heartbeat.models.Failure;
import edu.uc.labs.heartbeat.models.WebTask;
import edu.uc.labs.heartbeat.service.WebTaskService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/task")
public class TaskController {
    
    final static Logger log = LoggerFactory.getLogger(TaskController.class);
    

    @RequestMapping(method = RequestMethod.POST, value = "create")
    public void createWebTask(@RequestBody WebTask task){
        webTaskService.create(task);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public void updateWebTask(@RequestBody WebTask task){
        webTaskService.update(task);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "show/all")
    public @ResponseBody List<WebTask> getAll(){
        return webTaskService.getAll();
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "delete")
    public void deleteWebTask(@RequestBody WebTask task){
        webTaskService.delete(task);
    }
    
    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleDataException(GenericDataException e){
        return new Failure(e.getMessage());
    }
    
    
    
    @Autowired WebTaskService webTaskService;
}
