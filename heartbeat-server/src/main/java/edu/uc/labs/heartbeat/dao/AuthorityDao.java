package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.Authority;

public interface AuthorityDao extends Dao<Authority>  {
    
    Authority findByName(String name);
 
}
