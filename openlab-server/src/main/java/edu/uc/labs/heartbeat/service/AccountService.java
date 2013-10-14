package edu.uc.labs.heartbeat.service;

import edu.uc.labs.heartbeat.dao.AuthorityDao;
import edu.uc.labs.heartbeat.dao.WebUserDao;
import edu.uc.labs.heartbeat.exceptions.GenericDataException;
import edu.uc.labs.heartbeat.exceptions.NoUserException;
import edu.uc.labs.heartbeat.exceptions.UserExistsException;
import edu.uc.labs.heartbeat.models.Authority;
import edu.uc.labs.heartbeat.models.WebUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void updateWebUser(WebUser w){
        WebUser fromDB = webUserDao.findUserByName(w.getCn());
        if(fromDB == null){
            throw new NoUserException("Unable to find user " + w.getCn());
        } else {
            fromDB.setAuthorities(w.getAuthorities());
            fromDB.setEnabled(w.getEnabled());
            fromDB.setCn(w.getCn());
            try{
                webUserDao.update(w);
            } catch (RuntimeException e){
                throw new GenericDataException(e.getMessage());
            }
        }
    }
    
    @Transactional(readOnly=true)
    public List<WebUser> getAllWebUsers(){
        try {
            return webUserDao.getAll();
        }catch (RuntimeException e) {
            throw new GenericDataException(e.getMessage());
        }
    }
    
    public List<WebUser> getWebUsersWith(long role){
        return webUserDao.findUsersInRole(role);
    }

    @Transactional
    public void createAuthority(Authority a) {
        authDao.create(a);
    }
    
    @Transactional
    public List<Authority> getAllAuthorities(){
        return authDao.getAll();
    }

    @Transactional
    public Authority findAuthorityByName(String name) {
        return authDao.findByName(name);
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
    @Autowired
    AuthorityDao authDao;
    @Autowired
    WebUserDao webUserDao;
}
