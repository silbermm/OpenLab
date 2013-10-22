package co.silbersoft.openlab.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import co.silbersoft.openlab.models.Machine;
import co.silbersoft.openlab.models.MachineGroup;

@Repository
public class MachineDaoImpl extends AbstractDao<Machine> implements MachineDao {

    @Autowired
    public MachineDaoImpl(SessionFactory sf) {
        this.setSessionFactory(sf);
        this.sf = sf;
    }

    @Override
    public Machine findBySerialNumber(String serialNumber) {
        Machine m = (Machine) sf.getCurrentSession().createQuery("from Machine where serviceTag = :serviceTag").setString("serviceTag", serialNumber).uniqueResult();
        return m;
    }

    @Override
    public Machine findByUuid(String uuid) {
        Machine m = (Machine) sf.getCurrentSession().createQuery("from Machine where uid = :uuid").setString("uuid", uuid).uniqueResult();        
        return m;
    }

    @Override
    public boolean serialNumberExists(String serialNumber) {
        Machine m = findBySerialNumber(serialNumber);
        return (m == null) ? false : true;
    }

    @Override
    public Set<Machine> findByGroup(MachineGroup group) {
        List<Machine> machines = (List<Machine>) sf.getCurrentSession().createQuery("from Machine where group_id = :groupid").setLong("groupid", group.getGroupId()).list();
        return new HashSet<Machine>(machines);
    }
    
    private SessionFactory sf;
}
