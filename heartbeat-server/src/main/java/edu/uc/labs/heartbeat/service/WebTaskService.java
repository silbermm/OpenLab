package edu.uc.labs.heartbeat.service;

import edu.uc.labs.heartbeat.dao.WebTaskDao;
import edu.uc.labs.heartbeat.exceptions.GenericDataException;
import edu.uc.labs.heartbeat.models.WebTask;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebTaskService {
    
    public void newCloningTask(String uuid){
        WebTask t = new WebTask();
        t.setCompletionPercentage(new Long(0));
        t.setDescription("Cloning machine with uuid " + uuid );
        t.setName("Clone Task");
        t.setStatus("Starting...");
        this.create(t);
    }
   
    public void create(WebTask task){        
        try {
            webTaskDao.create(task);
        } catch (RuntimeException e){
            throw new GenericDataException(e.getMessage());
        } catch (Exception e){
            throw new GenericDataException(e.getMessage());
        }
    }
    
    public void update(WebTask task){
         try {
            webTaskDao.update(task);
        } catch (RuntimeException e){
            throw new GenericDataException(e.getMessage());
        } catch (Exception e){
            throw new GenericDataException(e.getMessage());
        }        
    }
    
    public List<WebTask> getAll(){
        return webTaskDao.getAll();
    }
    
    public void delete(WebTask task){
         try {
            webTaskDao.delete(task);
        } catch (RuntimeException e){
            throw new GenericDataException(e.getMessage());
        } catch (Exception e){
            throw new GenericDataException(e.getMessage());
        }        
    }
    
    @Autowired WebTaskDao webTaskDao;
    
}
