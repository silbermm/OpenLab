package edu.uc.labs.heartbeat.security;

import edu.uc.labs.heartbeat.models.WebUser;
import edu.uc.labs.heartbeat.service.WebUserService;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SamlUserDetailsService implements AuthenticationUserDetailsService {

    private WebUserService webUserService;
    
    @Override
    public UserDetails loadUserDetails(Authentication t) throws UsernameNotFoundException {
        String username = (String) t.getPrincipal();
        final WebUser user = getWebUserService().getUserByUsername(username);
        
        if (user == null){
            throw new UsernameNotFoundException("Cannot find username in the system");            
        }
        
        UserDetails ud = new UserDetails(){

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities();                
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return user.getCn();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
            
        };
        return ud;
    }

    public WebUserService getWebUserService() {
        return webUserService;
    }

    public void setWebUserService(WebUserService webUserService) {
        this.webUserService = webUserService;
    }
    
    
    
}
