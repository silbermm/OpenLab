package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.Authority;
import edu.uc.labs.heartbeat.models.WebUser;
import java.util.List;
import java.util.Set;


public interface WebUserDao extends Dao<WebUser> {
    
    WebUser findUserByName(String username);
    List<WebUser> findUsersInRole(long auth);
}
