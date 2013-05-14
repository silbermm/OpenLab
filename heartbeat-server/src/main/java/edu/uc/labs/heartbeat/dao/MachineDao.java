package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.Machine;

public interface MachineDao extends Dao<Machine> {

    Machine findBySerialNumber(String serialNumber);

    boolean serialNumberExists(String serialNumber);

    Machine findByUuid(String uuid);

}
