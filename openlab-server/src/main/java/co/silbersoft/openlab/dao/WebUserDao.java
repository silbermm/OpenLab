package co.silbersoft.openlab.dao;

import java.util.List;
import java.util.Set;

import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.WebUser;


public interface WebUserDao extends Dao<WebUser> {
    
    WebUser findUserByName(String username);
    List<WebUser> findUsersInRole(long auth);
}
