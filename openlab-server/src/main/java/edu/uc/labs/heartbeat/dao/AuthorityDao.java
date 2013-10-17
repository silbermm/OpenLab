package edu.uc.labs.heartbeat.dao;

import java.util.List;

import edu.uc.labs.heartbeat.models.Authority;

public interface AuthorityDao extends Dao<Authority>  {
    
    Authority findByName(String name);
 
}
