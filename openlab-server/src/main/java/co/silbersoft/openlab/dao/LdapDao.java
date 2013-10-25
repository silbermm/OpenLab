package co.silbersoft.openlab.dao;

import java.util.List;

import co.silbersoft.openlab.models.LdapUser;

public interface LdapDao {

	List<LdapUser> findByUsername(String username);
	List<LdapUser> findByEmployeeId(String id);

	
}
