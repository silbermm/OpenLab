package co.silbersoft.openlab.service.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.WebUser;
import co.silbersoft.openlab.service.AccountService;

@Service
@Transactional(readOnly = true)
public class ActiveDirectoryUserContextMapper implements UserDetailsContextMapper {

	private final String rolePrefix = "ROLE_";

	@Override
	@Transactional(readOnly = false)
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<? extends GrantedAuthority> authorities) {
		
		Set<Authority> defaultAuthorities = new HashSet<Authority>();
		Set<GrantedAuthority> finalAuthorities = new HashSet<GrantedAuthority>();
		WebUser webUser = webUserService.getUserByUsername(username);
		Authority authority = webUserService.findAuthorityByName("domain_user");
		defaultAuthorities.add(authority);
		if (webUser == null) {
			// Create the username in the database
			webUser = new WebUser();
			webUser.setCn(username);
			webUser.setEnabled(true);
			webUser.setDn(ctx.getDn().toString());			
			webUser.setAuthorities(defaultAuthorities);								
			webUserService.createWebUser(webUser);
		}

		Set<Authority> aSet = webUser.getAuthorities();
		for (Authority auth : aSet) {
			String role = auth.getAuthority();
			role = role.toUpperCase();
			if (role.startsWith("ROLE_")) {
				finalAuthorities.add(new SimpleGrantedAuthority(role));
			} else {
				finalAuthorities.add(new SimpleGrantedAuthority(rolePrefix
						+ role));
			}			
		}
		finalAuthorities.addAll(authorities);
		return new User(username, "", webUser.getEnabled(), true, true, true,finalAuthorities);
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {		
		
	}

	@Autowired
	AccountService webUserService;

}
