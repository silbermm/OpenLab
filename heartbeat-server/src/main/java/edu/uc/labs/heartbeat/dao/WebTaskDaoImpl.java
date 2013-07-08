package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.WebTask;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;



@Repository
public class WebTaskDaoImpl extends AbstractDao<WebTask> implements WebTaskDao {

    public WebTaskDaoImpl(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

}
