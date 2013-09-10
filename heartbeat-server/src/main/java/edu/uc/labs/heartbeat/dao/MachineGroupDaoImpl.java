package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.MachineGroup;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MachineGroupDaoImpl extends AbstractDao<MachineGroup> implements MachineGroupDao {

    @Autowired
    public MachineGroupDaoImpl(SessionFactory sf){
        this.setSessionFactory(sf);
        this.sf = sf;
    }

    @Override
    public MachineGroup getGroupByShortName(String name) {
        return (MachineGroup) sf.getCurrentSession().createQuery("from MachineGroup where name=:name").setString("name", name).uniqueResult();
    }
    
    private SessionFactory sf;
}
