package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.RemoteImageTask;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RemoteImagingDaoImpl extends AbstractDao<RemoteImageTask> implements RemoteImagingDao {
    
   final private Logger log = LoggerFactory.getLogger(RemoteImagingDaoImpl.class);
    
    public RemoteImagingDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public RemoteImageTask getTaskBySerialAndMac(String serial, String mac) {                
        Query q = getSession().createQuery("from RemoteImageTask where serialNumber=:serial or mac like :mac");
        q.setString("serial", serial);
        q.setString("mac", "%" + mac + "%");
        return (RemoteImageTask) q.uniqueResult();        
    }

    @Override
    public void expireTasksOlderThan(int minutes) {        
        Date d = new Date(System.currentTimeMillis() - minutes * 60000);        
        Query q = getSession().createQuery("delete from RemoteImageTask r where r.created < :expired");
        q.setTimestamp("expired", d);
        q.executeUpdate();        
    }
                
    
}
