package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.MachineGroup;

public interface MachineGroupDao extends Dao<MachineGroup> {

    MachineGroup getGroupByShortName(String name);

}
