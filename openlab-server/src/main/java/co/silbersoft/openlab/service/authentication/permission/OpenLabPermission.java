package co.silbersoft.openlab.service.authentication.permission;

import org.springframework.security.core.Authentication;

public interface OpenLabPermission {
	boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
