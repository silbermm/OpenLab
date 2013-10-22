package co.silbersoft.openlab.dao;


import java.util.Set;

import co.silbersoft.openlab.models.*;

public interface MachineDao extends Dao<Machine> {

    Machine findBySerialNumber(String serialNumber);

    boolean serialNumberExists(String serialNumber);

    Machine findByUuid(String uuid);

    Set<Machine> findByGroup(MachineGroup group);

}
