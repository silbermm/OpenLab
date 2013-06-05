package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.MachineGroup;
import org.hibernate.SessionFactory;

public class MachineGroupDaoImpl extends AbstractDao<MachineGroup> implements MachineGroupDao {

    public MachineGroupDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public MachineGroup getGroupByShortName(String name) {
        return (MachineGroup) getSession().createQuery("from MachineGroup where name=:name").setString("name", name).uniqueResult();
    }
}
