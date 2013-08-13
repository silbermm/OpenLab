package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.WebUser;


public interface WebUserDao extends Dao<WebUser> {
    
    WebUser findUserByName(String username);
    
}
