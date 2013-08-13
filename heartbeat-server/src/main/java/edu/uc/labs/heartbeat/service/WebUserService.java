package edu.uc.labs.heartbeat.service;

import edu.uc.labs.heartbeat.dao.AuthorityDao;
import edu.uc.labs.heartbeat.dao.WebUserDao;
import edu.uc.labs.heartbeat.models.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebUserService {
  
    @Transactional(readOnly=true)
    public WebUser getUserByUsername(String name){
        return webUserDao.findUserByName(name);        
    }
    
    
    @Autowired AuthorityDao authDao;
    @Autowired WebUserDao webUserDao;
    
}
