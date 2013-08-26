package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.Authority;
import edu.uc.labs.heartbeat.models.WebUser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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

    @Override
    public List<WebUser> findUsersInRole(long auth) {
        Criteria crit = getSession().createCriteria(WebUser.class);
        crit.createAlias("authorities", "authAlias");
        crit.add(Restrictions.eq("authAlias.authorityId", auth));
        List<WebUser> users = crit.list();
        return users;
    }
    
}
