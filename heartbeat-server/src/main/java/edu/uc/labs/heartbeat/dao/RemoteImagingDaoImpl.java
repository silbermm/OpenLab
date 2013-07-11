package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.RemoteImageTask;
import org.hibernate.Query;
import org.hibernate.SessionFactory;


public class RemoteImagingDaoImpl extends AbstractDao<RemoteImageTask> implements RemoteImagingDao {
    
    public RemoteImagingDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public RemoteImageTask getTaskBySerialAndMac(String serial, String mac) {                
        Query q = getSession().createQuery("from RemoteImageTask where serial=:serial or mac like :mac");
        q.setString("serial", serial);
        q.setString("mac", "%" + mac + "%");
        return (RemoteImageTask) q.uniqueResult();        
    }

    @Override
    public void deleteTasksByMinutes(int minutes) {
        Query q = getSession().createQuery("delete from RemoteImageTask where created < date_sub(now(), INTERVAL :minutes MINUTE");
        q.setInteger("minutes", minutes);
        q.executeUpdate();                        
    }
                
    
}
