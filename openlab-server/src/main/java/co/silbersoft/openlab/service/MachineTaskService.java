package co.silbersoft.openlab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.silbersoft.openlab.dao.MachineTaskDao;
import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.models.MachineTask;

@Service
public class MachineTaskService {
    
    private List<MachineTask> rebootTasks = new ArrayList<MachineTask>();    

    public MachineTask newCloningTask(String uuid) {
        MachineTask t = new MachineTask();
        t.setCompletionPercentage(new Long(0));
        t.setDescription("Cloning machine with uuid " + uuid);
        t.setName("Clone Task");
        t.setStatus("Starting...");
        this.create(t);
        return t;
    }
    
    public MachineTask newRebootTask(String uuid) {
        MachineTask t = new MachineTask();
        t.setCompletionPercentage(new Long(0));
        t.setDescription("Rebooting machine with uuid " + uuid);
        t.setName("Reboot Task");
        t.setStatus("Starting...");
        this.create(t);
        return t;
    }

    public MachineTask getById(int id) {
        try {
            return webTaskDao.get(id);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }

    public void create(MachineTask task) {
        try {
            webTaskDao.create(task);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }
        
    public void update(MachineTask task) {
        try {
            webTaskDao.update(task);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }

    public List<MachineTask> getAll() {
        return webTaskDao.getAll();
    }

    public void delete(MachineTask task) {
        try {
            webTaskDao.delete(task);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }
    @Autowired
    MachineTaskDao webTaskDao;
}
