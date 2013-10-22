package co.silbersoft.openlab.dao;

import java.util.List;

import co.silbersoft.openlab.models.Authority;

public interface AuthorityDao extends Dao<Authority>  {
    
    Authority findByName(String name);
    List<Authority> findByUser(String username);
 
}
