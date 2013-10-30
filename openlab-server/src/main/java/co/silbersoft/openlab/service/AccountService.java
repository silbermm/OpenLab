package co.silbersoft.openlab.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.silbersoft.openlab.dao.AuthorityDao;
import co.silbersoft.openlab.dao.LdapDao;
import co.silbersoft.openlab.dao.WebUserDao;
import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.exceptions.NoUserException;
import co.silbersoft.openlab.exceptions.UserExistsException;
import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.LdapUser;
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
		try {		
			log.debug("trying to remove all authorities first");
			for(Authority a : w.getAuthorities()){
				this.removeAuthFromUser(a.getAuthorityId(), w.getUserId());
			}	
			webUserDao.delete(w);		
		} catch (RuntimeException e) {
			throw new GenericDataException(e.getMessage());
		} catch (Exception e){
			throw new GenericDataException(e.getMessage());
		}
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
	
	@Transactional(readOnly=false)
	public void removeAuthFromUser(long authId, long userId){
		try {
			WebUser u = webUserDao.get(userId);
			log.debug("found user " + u.getCn());
			Authority a = authDao.get(authId);
			log.debug("found authority " + a.getAuthority());
			Set<Authority> userAuths = u.getAuthorities();
			log.debug("searching user auths for specific authority... ");			
			Authority toRemove = null;			
			for(Authority ua : userAuths){
				log.debug("looking at " + ua.getAuthority());
				if(ua.getAuthorityId() == a.getAuthorityId()){
					log.debug("found the right one!");
					toRemove = ua;
				}
			}
			if(toRemove != null) userAuths.remove(toRemove);
			u.setAuthorities(userAuths);
			log.debug("updateing user");
			webUserDao.update(u);
		}catch (RuntimeException e){
			throw new GenericDataException("something went wrong while removing authorities! " + e.getClass().getCanonicalName() +": " +  e.getMessage());
		}catch (Exception e){
			throw new GenericDataException("something went wrong while removing authorities! " + e.getClass().getCanonicalName() +": " +  e.getMessage());
		}
	}
	
	public List<LdapUser> searchForUser(String searchTerm){
		try {
			List<LdapUser> found = ldapDao.findByUsername(searchTerm);
			if(!found.isEmpty()){
				log.debug("found " + found.size() + " objects like " + searchTerm);
				return found;
			} else {
				log.debug("Did not find any username like " + searchTerm);
				List<LdapUser> mNumber = ldapDao.findByEmployeeId(searchTerm);
				if(!mNumber.isEmpty()){
					log.debug("found " + mNumber.size() + " objects like " + searchTerm);
					return mNumber;
				} else {
					log.debug("unable to find any objects like " + searchTerm);
					return null;
				}
			}				
		} catch (RuntimeException e){
			throw new NameNotFoundException(e.getMessage());
		} catch (Exception e){
			throw new NameNotFoundException(e.getMessage());
		}
		
	}
	
	
	private static final Logger log = LoggerFactory.getLogger(AccountService.class); 
	@Autowired AuthorityDao authDao;
	@Autowired WebUserDao webUserDao;
	@Autowired LdapDao ldapDao;
}
