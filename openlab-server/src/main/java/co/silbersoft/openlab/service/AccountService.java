package co.silbersoft.openlab.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.silbersoft.openlab.dao.AuthorityDao;
import co.silbersoft.openlab.dao.WebUserDao;
import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.exceptions.NoUserException;
import co.silbersoft.openlab.exceptions.UserExistsException;
import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.WebUser;

@Service
@Transactional
public class AccountService {

	@Transactional(readOnly = true)
	public WebUser getUserByUsername(String name) {
		return webUserDao.findUserByName(name);
	}

	@Transactional
	public void createWebUser(WebUser w) {
		if (webUserDao.findUserByName(w.getCn()) != null) {
			throw new UserExistsException("User already exists");
		} else {
			webUserDao.create(w);
		}
	}

	@Transactional
	public void updateWebUser(WebUser w) {
		WebUser fromDB = webUserDao.findUserByName(w.getCn());
		if (fromDB == null) {
			throw new NoUserException("Unable to find user " + w.getCn());
		} else {
			fromDB.setAuthorities(w.getAuthorities());
			fromDB.setEnabled(w.getEnabled());
			fromDB.setCn(w.getCn());
			try {
				webUserDao.update(w);
			} catch (RuntimeException e) {
				throw new GenericDataException(e.getMessage());
			}
		}
	}

	@Transactional(readOnly = true)
	public List<WebUser> getAllWebUsers() {
		try {
			return webUserDao.getAll();
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public List<WebUser> getAllEnabledWebUsers() {
		try {
			return webUserDao.findAllEnabled(true);
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public List<WebUser> getAllDisabledWebUsers() {
		try {
			return webUserDao.findAllEnabled(false);
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	public List<WebUser> getWebUsersWith(long role) {
		return webUserDao.findUsersInRole(role);
	}

	@Transactional
	public void createAuthority(Authority a) {
		authDao.create(a);
	}

	@Transactional
	public List<Authority> getAllAuthorities() {
		return authDao.getAll();
	}

	@Transactional
	public List<Authority> getAuthoritiesByUser(String username) {
		try {
			return authDao.findByUser(username);
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		} catch (Exception e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	@Transactional
	public Authority findAuthorityByName(String name) {
		return authDao.findByName(name);
	}

	@Transactional
	public WebUser findUserById(long id) {
		try {
			return webUserDao.get(id);
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		} catch (Exception e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	@Transactional
	public void deleteWebUser(WebUser w) {
		webUserDao.delete(w);
	}

	@Transactional
	public void deleteWebUser(long id) {
		try {
			webUserDao.deleteById(id);
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		}
	}

	@Transactional
	public void deleteAuthority(Authority a) {
		authDao.delete(a);
	}

	@Transactional(readOnly = false)
	public boolean addAuthToUser(long authId, long userId) {
		try {
			WebUser u = webUserDao.get(userId);
			Authority a = authDao.get(authId);
			u.addAuthority(a);
			webUserDao.update(u);
			return true;
		} catch (RuntimeException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Transactional(readOnly=false)
	public void enableUser(long userId, boolean enable){
		try{
			WebUser u = webUserDao.get(userId);
			u.setEnabled(enable);
			webUserDao.update(u);
		}catch (RuntimeException e){
			throw new GenericDataException(e.getMessage());
		}catch (Exception e){
			throw new GenericDataException(e.getMessage());
		}
	}
	
	public void removeAuthFromUser(long authId, long userId){
		try {
			WebUser u = webUserDao.get(userId);
			Authority a = authDao.get(authId);
			Set<Authority> userAuths = u.getAuthorities();
			for(Authority ua : userAuths){
				if(ua.getAuthorityId() == a.getAuthorityId()){
					userAuths.remove(ua);
				}
			}
			u.setAuthorities(userAuths);
			webUserDao.update(u);
		}catch (RuntimeException e){
			throw new GenericDataException(e.getMessage());
		}catch (Exception e){
			throw new GenericDataException(e.getMessage());
		}
	}
	
	public void searchForUser(String searchTerm){
		LdapTemplate ldapTemplate = new LdapTemplate(ldapContext);
		AndFilter filter = new AndFilter();
	}
	
	
	

	@Autowired
	AuthorityDao authDao;
	@Autowired
	WebUserDao webUserDao;
	@Autowired
	LdapContextSource ldapContext;
}
