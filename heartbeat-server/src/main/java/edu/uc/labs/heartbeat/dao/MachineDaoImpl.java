package edu.uc.labs.heartbeat.dao;


import edu.uc.labs.heartbeat.models.Machine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MachineDaoImpl extends AbstractDao<Machine> implements MachineDao {

    public MachineDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }


    @Override
    public Machine findBySerialNumber(String serialNumber) {
        return (Machine) getSession().createQuery("from Machine where serviceTag = ?").setParameter(1, serialNumber).uniqueResult();
    }

    public Machine findByUuid(String uuid){
        return (Machine) getSession().createQuery("from Machine where uid = :uuid").setString("uuid", uuid).uniqueResult();

    }

    @Override
    public boolean serialNumberExists(String serialNumber) {
        Machine m = findBySerialNumber(serialNumber);
        return (m == null) ? false : true;
    }
}
