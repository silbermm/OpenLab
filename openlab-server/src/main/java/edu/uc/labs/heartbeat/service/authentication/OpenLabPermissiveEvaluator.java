package edu.uc.labs.heartbeat.service.authentication;

import java.io.Serializable;
import java.util.*;

import edu.uc.labs.heartbeat.exceptions.PermissionNotDefinedException;
import edu.uc.labs.heartbeat.service.authentication.permission.*;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="permissionEvaluator")
public class OpenLabPermissiveEvaluator implements PermissionEvaluator {
	
	private Map<String, OpenLabPermission> permissionNameToPermissionMap = new HashMap<String, OpenLabPermission>();	

	public OpenLabPermissiveEvaluator(Map<String, OpenLabPermission> permissionNameToPermissionMap){		
		this.permissionNameToPermissionMap = permissionNameToPermissionMap;
	}
	
	@Override
	@Transactional
	public boolean hasPermission(Authentication auth, Object domainObj, Object permission) {
		boolean hasPermission = false;
        if (canHandle(auth, domainObj, permission)) {
            hasPermission = checkPermission(auth, domainObj, (String) permission);
        }
        return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId ,String targetType, Object permissions) {
		return false;
	}
	
	private boolean checkPermission(Authentication authentication, Object targetDomainObject, String permissionKey) {
        verifyPermissionIsDefined(permissionKey);
        OpenLabPermission permission = permissionNameToPermissionMap.get(permissionKey);
        return permission.isAllowed(authentication, targetDomainObject);
    }
	
	private boolean canHandle(Authentication authentication, Object targetDomainObject, Object permission) {
        return targetDomainObject != null && authentication != null && permission instanceof String;
    }
	
	private void verifyPermissionIsDefined(String permissionKey) {
        if (!permissionNameToPermissionMap.containsKey(permissionKey)) {
            throw new PermissionNotDefinedException("No permission with key " + permissionKey + " is defined in " + this.getClass().toString());
        }
    }


}
