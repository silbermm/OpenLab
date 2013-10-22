package co.silbersoft.openlab.dao;


import co.silbersoft.openlab.models.MachineGroup;

public interface MachineGroupDao extends Dao<MachineGroup> {

    MachineGroup getGroupByShortName(String name);

}
