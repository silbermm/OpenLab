package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.Machine;
import edu.uc.labs.heartbeat.models.MachineGroup;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class MachineDaoImpl extends AbstractDao<Machine> implements MachineDao {

    public MachineDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }


    @Override
    public Machine findBySerialNumber(String serialNumber) {
        return (Machine) getSession().createQuery("from Machine where serviceTag = :serviceTag").setString("serviceTag", serialNumber).uniqueResult();
    }

	@Override
	public Machine findByUuid(String uuid){
        return (Machine) getSession().createQuery("from Machine where uid = :uuid").setString("uuid", uuid).uniqueResult();
    }

    @Override
    public boolean serialNumberExists(String serialNumber) {
        Machine m = findBySerialNumber(serialNumber);
        return (m == null) ? false : true;
    }

	@Override
	public Set<Machine> findByGroup(MachineGroup group) {
        List<Machine> machines = (List<Machine>) getSession().createQuery("from Machine where group_id = :groupid").setLong("groupid", group.getGroupId()).list();
        return new HashSet<Machine>(machines);
	}
}
