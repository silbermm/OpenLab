package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.dao.MachineDao;
import edu.uc.labs.heartbeat.dao.MachineGroupDao;
import edu.uc.labs.heartbeat.models.ClientMachine;
import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.MachineGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Machine getMachineByUuid(String uuid){
        return machineDao.findByUuid(uuid);
    }

		public Machine getMachineById(Long id){
				return machineDao.get(id);
		}

    public List<MachineGroup> getAllMachineGroups(){
        return machineGroupDao.getAll();
    }

	public MachineGroup getGroupById(Long id){
	    MachineGroup g =  machineGroupDao.get(id);
		g.setMachines(machineDao.findByGroup(g));
        return g;
	}

    @Transactional(readOnly = false)
    public boolean updateMachine(ClientMachine cm, String ipaddress){
        try {
            if(machineDao.serialNumberExists(cm.getSerialNumber())){
                // The machine already exists in the database, we just need to update it...
                log.debug("Machine already exists...");
                Machine m = machineDao.findBySerialNumber(cm.getSerialNumber());
                m.setIpAddress(ipaddress);
                m.setUid(cm.getUuid());
                m.setManufacturer(cm.getManufacturer());
                m.setModel(cm.getModel());
                m.setOsVersion(cm.getOsVersion());
                m.setOs(cm.getOs());
                m.setMac(cm.getMac());
                m.setCurrentUser(cm.getCurrentUser());
                m.setName(cm.getName());
                log.info("updating machine " + m);
                machineDao.update(m);
            } else {
                // This is a new machine
                Machine m = new Machine();
                m.setIpAddress(ipaddress);
                m.setCurrentUser(cm.getCurrentUser());
                m.setMac(cm.getMac());
                m.setManufacturer(cm.getManufacturer());
                m.setModel(cm.getModel());
                m.setSerialNumber(cm.getSerialNumber());
                m.setName(cm.getName());
                m.setOs(cm.getOs());
                m.setOsVersion(cm.getOsVersion());
                m.setUid(cm.getUuid());
                log.debug("Creating Machine " + m);
                machineDao.create(m);
            }
        }catch (RuntimeException e){
            log.error(e.getClass().getCanonicalName() + ": " + e.getMessage());
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

    public void setMachineGroupDao(MachineGroupDao machineGroupDao){
        this.machineGroupDao = machineGroupDao;
    }

    final static Logger log = LoggerFactory.getLogger(HeartbeatService.class);
    private Config config;
    private MachineDao machineDao;
    private MachineGroupDao machineGroupDao;

}
