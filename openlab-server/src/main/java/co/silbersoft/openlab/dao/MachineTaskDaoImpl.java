package co.silbersoft.openlab.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.silbersoft.openlab.models.MachineTask;



@Repository
public class MachineTaskDaoImpl extends AbstractDao<MachineTask> implements MachineTaskDao {

    @Autowired
    public MachineTaskDaoImpl(SessionFactory sf) {
        this.setSessionFactory(sf);
        this.sf = sf;
    }

    private SessionFactory sf;    
}
