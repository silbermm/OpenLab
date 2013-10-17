package edu.uc.labs.heartbeat.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.uc.labs.heartbeat.models.Permission;

@Repository
public class PermissionDaoImpl extends AbstractDao<Permission> implements PermissionDao {

	@Autowired
	public PermissionDaoImpl(SessionFactory sf){
		this.setSessionFactory(sf);
	}
	

	
}
