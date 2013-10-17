package edu.uc.labs.heartbeat.service.authentication.permission;

import org.springframework.security.core.Authentication;

public interface OpenLabPermission {
	boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
