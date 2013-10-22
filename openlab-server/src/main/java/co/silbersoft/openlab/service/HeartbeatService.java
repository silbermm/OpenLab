package co.silbersoft.openlab.service;

import co.silbersoft.openlab.dao.MachineDao;
import co.silbersoft.openlab.dao.MachineGroupDao;
import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.models.Machine;
import co.silbersoft.openlab.models.MachineGroup;

import com.typesafe.config.Config;

import edu.uc.labs.heartbeat.domain.ClientMachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional(readOnly = true)
public class HeartbeatService {

    public HeartbeatService() {
    }

    public HeartbeatService(Config config) {
        this.config = config;
    }

    public List<Machine> getAllMachines() {
        List<Machine> machines = machineDao.getAll();
        return machines;
    }

    public Machine getMachineByUuid(String uuid) {
        return machineDao.findByUuid(uuid);
    }

    public Machine getMachineById(Long id) {
        return machineDao.get(id);
    }

    public Machine getMachineBySerial(String serial) {
        try {
            return machineDao.findBySerialNumber(serial);
        } catch (RuntimeException ex) {
            throw new GenericDataException(ex.getMessage());
        } catch (Exception ex) {
            throw new GenericDataException(ex.getMessage());
        }
    }

    public Set<Machine> getMachinesInGroup(Long groupId) {
        try {
            MachineGroup mg = machineGroupDao.get(groupId);
            return machineDao.findByGroup(mg);
        } catch (RuntimeException ex) {
            throw new GenericDataException(ex.getMessage());
        } catch (Exception ex) {
            throw new GenericDataException(ex.getMessage());
        }
    }

    public List<MachineGroup> getAllMachineGroups() {
        List<MachineGroup> groups = machineGroupDao.getAll();
        for (MachineGroup g : groups) {
            g.setMachines(machineDao.findByGroup(g));
        }
        return groups;
    }

    public MachineGroup getGroupById(Long id) {
        MachineGroup g = machineGroupDao.get(id);
        g.setMachines(machineDao.findByGroup(g));
        return g;
    }

    @Transactional(readOnly=false)
    public long createGroup(MachineGroup m) {
        try {
            machineGroupDao.create(m);
            MachineGroup n = machineGroupDao.getGroupByShortName(m.getName());
            return n.getGroupId();
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }
    
    @Transactional(readOnly=false)
    public void updateGroup(MachineGroup m){
        try {
            machineGroupDao.update(m);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }
    
    @Transactional(readOnly=false)
    public void deleteGroup(MachineGroup m){
        try {
            machineGroupDao.delete(m);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void moveMachine(String uuid, long toGroupId) {
        try {
            Machine m = getMachineByUuid(uuid);
            MachineGroup group = getGroupById(toGroupId);
            m.setGroup(group);
            machineDao.update(m);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void moveMachine(long machineId, long groupId) {
        try {
            Machine m = machineDao.get(machineId);
            MachineGroup group = machineGroupDao.get(groupId);
            m.setGroup(group);
            machineDao.update(m);
        } catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            throw new GenericDataException(e.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteMachine(Machine m) {
        log.info("calling delete from the dao");
        machineDao.delete(m);
    }

    @Transactional(readOnly = false)
    public boolean updateMachine(ClientMachine cm, String ipaddress) {
        try {
            if (machineDao.serialNumberExists(cm.getSerialNumber())) {
                // The machine already exists in the database, we just need to update it...
                log.debug("Machine already exists...");
                Machine m = machineDao.findBySerialNumber(cm.getSerialNumber());
                m.setIpAddress(ipaddress);
                m.setUid(cm.getUuid());
                m.setManufacturer(cm.getManufacturer());
                m.setModel(cm.getModel());
                m.setOsVersion(cm.getOsVersion());
                m.setOs(cm.getOs());
                if (cm.getOs().startsWith("Mac")) {
                    m.setMacName(cm.getName());
                } else {
                    m.setName(cm.getName());
                }
                m.setMac(cm.getMac());
                m.setCurrentUser(cm.getCurrentUser());
                m.setPartition1(cm.getPartition1());
                m.setPartition2(cm.getPartition2());
                m.setPartition3(cm.getPartition3());
                m.setPartition4(cm.getPartition4());
                m.setFacility(cm.getFacility());
                log.info("updating machine " + m);
                machineDao.update(m);
            } else {
                // This is a new machine
                Machine m = new Machine();
                MachineGroup mg = machineGroupDao.getGroupByShortName("unknown");
                m.setIpAddress(ipaddress);
                m.setCurrentUser(cm.getCurrentUser());
                m.setMac(cm.getMac());
                m.setManufacturer(cm.getManufacturer());
                m.setModel(cm.getModel());
                m.setSerialNumber(cm.getSerialNumber());
                m.setOs(cm.getOs());
                if (cm.getOs().startsWith("Mac")) {
                    m.setMacName(cm.getName());
                } else {
                    m.setName(cm.getName());
                }
                m.setGroup(mg);
                m.setOsVersion(cm.getOsVersion());
                m.setUid(cm.getUuid());
                m.setPartition1(cm.getPartition1());
                m.setPartition2(cm.getPartition2());
                m.setPartition3(cm.getPartition3());
                m.setPartition4(cm.getPartition4());
                m.setFacility(cm.getFacility());
                log.debug("Creating Machine " + m);
                machineDao.create(m);
            }
        } catch (RuntimeException e) {
            log.error(e.getClass().getCanonicalName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void updateMachineRecord(String uuid) {
        Machine m = machineDao.findByUuid(uuid);
        m.setLastSeen(new Date(System.currentTimeMillis()));
        machineDao.update(m);
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setMachineDao(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    public void setMachineGroupDao(MachineGroupDao machineGroupDao) {
        this.machineGroupDao = machineGroupDao;
    }
    final static Logger log = LoggerFactory.getLogger(HeartbeatService.class);
    @Autowired
    Config config;
    @Autowired
    MachineDao machineDao;
    @Autowired
    MachineGroupDao machineGroupDao;
}
