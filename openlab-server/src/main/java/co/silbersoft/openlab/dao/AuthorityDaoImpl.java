package co.silbersoft.openlab.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.silbersoft.openlab.models.Authority;

@Repository
public class AuthorityDaoImpl extends AbstractDao<Authority> implements AuthorityDao {
    
    @Autowired
    public AuthorityDaoImpl(SessionFactory sf){
        this.setSessionFactory(sf);
    }

    @Override
    public Authority findByName(String name) {        
        return (Authority)getSession().createQuery("from Authority where authority=:name").setString("name", name).uniqueResult();
    }

	@Override
	public List<Authority> findByUser(String username) {
		return (List<Authority>)getSession().createQuery("from Authority a join a.webUsers u where u.cn = :username").setString("username", username).list();		
	}	
        
}
