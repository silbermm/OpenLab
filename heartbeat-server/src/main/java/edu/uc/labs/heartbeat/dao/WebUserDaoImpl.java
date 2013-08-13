package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.WebUser;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class WebUserDaoImpl extends AbstractDao<WebUser> implements WebUserDao{
  
    public WebUserDaoImpl(SessionFactory sf){
        this.setSessionFactory(sf);
    }

    @Override
    public WebUser findUserByName(String username) {
        return (WebUser) getSession().createQuery("from WebUser where cn = :cn").setString("cn", username).uniqueResult();
    }
    
}
