package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.RemoteImageTask;
import org.hibernate.SessionFactory;


public class RemoteImagingDaoImpl extends AbstractDao<RemoteImageTask> implements RemoteImagingDao {
    
    public RemoteImagingDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
    
    
    
}
