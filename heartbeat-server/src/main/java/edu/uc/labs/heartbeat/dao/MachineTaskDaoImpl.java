package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.MachineTask;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class MachineTaskDaoImpl extends AbstractDao<MachineTask> implements MachineTaskDao {

    @Autowired
    public MachineTaskDaoImpl(SessionFactory sf) {
        this.setSessionFactory(sf);
        this.sf = sf;
    }

    private SessionFactory sf;    
}
