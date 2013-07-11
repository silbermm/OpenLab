package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.MachineTask;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;



@Repository
public class MachineTaskDaoImpl extends AbstractDao<MachineTask> implements MachineTaskDao {

    public MachineTaskDaoImpl(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

}
