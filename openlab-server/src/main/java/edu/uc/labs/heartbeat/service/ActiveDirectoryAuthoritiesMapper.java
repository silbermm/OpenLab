package edu.uc.labs.heartbeat.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class ActiveDirectoryAuthoritiesMapper implements
		GrantedAuthoritiesMapper {

	private final String rolePrefix = "ROLE_";
	private final GrantedAuthority defaultRole = new SimpleGrantedAuthority(
			"ROLE_USER");

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(
			Collection<? extends GrantedAuthority> arg0) {

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		for (GrantedAuthority auth : arg0) {
			String role = auth.getAuthority();
			role = role.toUpperCase();
                        if(role.startsWith("ROLE_")){
                            authorities.add(new SimpleGrantedAuthority(role));
                        }else {
                            authorities.add(new SimpleGrantedAuthority(rolePrefix + role));
                        }
			
		}
		authorities.add(defaultRole);
		return authorities;
	}

}
