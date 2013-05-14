package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.models.Machine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HeartbeatService {

    public HeartbeatService(){

    }

    public HeartbeatService(Config config){
        this.config = config;
    }

    public List<Machine> getAllMachines(){
        return machineDao.getAll();
    }

    @Transactional(readOnly = false)
    public boolean updateMachine(Machine m){
        try {
            if(machineDao.serialNumberExists(m.getSerialNumber())){
                machineDao.update(m);
            } else {
                machineDao.create(m);
            }
        }catch (RuntimeException e){
            return false;
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void updateMachineRecord(String uuid){
        Machine m = machineDao.findByUuid(uuid);
        m.setLastSeen(new Date(System.currentTimeMillis()));
        machineDao.update(m);
    }

    public void setConfig(Config config){
        this.config = config;
    }

    public void setMachineDao(MachineDao machineDao){
        this.machineDao = machineDao;
    }

    private Config config;
    private MachineDao machineDao;

}
