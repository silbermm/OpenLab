package edu.uc.labs.heartbeat.service.authentication.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.Collection;

import edu.uc.labs.heartbeat.dao.MachineGroupDao;
import edu.uc.labs.heartbeat.models.Authority;
import edu.uc.labs.heartbeat.models.MachineGroup;
import edu.uc.labs.heartbeat.models.Permission;

@Service
@Transactional(readOnly=true)
public class MachineGroupPermission implements OpenLabPermission {	
	
	public MachineGroupPermission(){}
	
	@Override
	public boolean isAllowed(Authentication authentication,Object targetDomainObject) {
		log.debug("Checking if user has permission to group with id: " + targetDomainObject);
		boolean hasPermission = false;
        if (isAuthenticated(authentication) && isGroupId(targetDomainObject)) {            
            MachineGroup group = machineGroupDao.get((Long) targetDomainObject);        	
            User user = getLogin(authentication);
        	Collection<GrantedAuthority> userAuths = user.getAuthorities();     	
        	for(GrantedAuthority userAuth : userAuths){        		
        		// Is the user in the one of the right Roles?
        		for(Authority groupAuth : group.getAuthorities()){
        			if (groupAuth.getAuthority().equals(userAuth.getAuthority())){
        				// Does this role have groupAdmin permissions?
        				for(Permission p : groupAuth.getPermissions()){
        					if(p.getPermission().equals("group_admin")){
        						hasPermission = true;
        					}
        				}
        			}
        		}        		
        	}        	
        }
        return hasPermission;		
	}
		
	private User getLogin(Authentication authentication) {        
        return (User) authentication.getPrincipal();
    }
	
	private boolean isGroupId(Object targetDomainObject) {
        return targetDomainObject instanceof Long && (Long) targetDomainObject > 0;
    }
 
    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof User;
    }
	
    private static final Logger log = LoggerFactory.getLogger(MachineGroupPermission.class);
	@Autowired MachineGroupDao machineGroupDao;

}
