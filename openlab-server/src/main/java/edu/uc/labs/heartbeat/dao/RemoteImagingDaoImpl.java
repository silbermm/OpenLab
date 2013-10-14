package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.RemoteImageTask;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteImagingDaoImpl extends AbstractDao<RemoteImageTask> implements RemoteImagingDao {
    
   final private Logger log = LoggerFactory.getLogger(RemoteImagingDaoImpl.class);
    
    @Autowired
    public RemoteImagingDaoImpl(SessionFactory sf){
        this.setSessionFactory(sf);
        this.sf = sf;
    }

    @Override
    public RemoteImageTask getTaskBySerialAndMac(String serial, String mac) {                
        Query q = sf.getCurrentSession().createQuery("from RemoteImageTask where serialNumber=:serial or mac like :mac");
        q.setString("serial", serial);
        q.setString("mac", "%" + mac + "%");
        return (RemoteImageTask) q.uniqueResult();        
    }

    @Override
    public void expireTasksOlderThan(int minutes) {        
        Date d = new Date(System.currentTimeMillis() - minutes * 60000);        
        Query q = sf.getCurrentSession().createQuery("delete from RemoteImageTask r where r.created < :expired");
        q.setTimestamp("expired", d);
        q.executeUpdate();        
    }
         
    private SessionFactory sf;
    
}
