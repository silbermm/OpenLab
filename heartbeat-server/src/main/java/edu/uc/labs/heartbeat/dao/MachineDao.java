package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.*;
import java.util.Set;

public interface MachineDao extends Dao<Machine> {

    Machine findBySerialNumber(String serialNumber);

    boolean serialNumberExists(String serialNumber);

    Machine findByUuid(String uuid);

    Set<Machine> findByGroup(MachineGroup group);

}
