package edu.uc.labs.heartbeat.config;

import edu.uc.labs.heartbeat.service.ActiveDirectoryAuthoritiesMapper;
import edu.uc.labs.heartbeat.service.ActiveDirectoryUserContextMapper;
import edu.uc.labs.heartbeat.service.ActiveDirectoryUserDetailsService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
           .authorizeRequests()
                .antMatchers("/login", "/").permitAll()
                //.antMatchers("/admin/**").hasRole("ADMIN") // #6
                .anyRequest().authenticated()
           .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/#/home")
                .permitAll()
           .and()
                .rememberMe().key("98jeij01101SdfiwA1294)").userDetailsService(userDetailsService()).tokenRepository(tokenRepo())
           .and()
                .httpBasic();
    }
    
    @Bean
    public PersistentTokenRepository tokenRepo(){
        JdbcTokenRepositoryImpl jtri = new JdbcTokenRepositoryImpl();
        jtri.setDataSource(dataSource);
        return jtri;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider adProvider = new ActiveDirectoryLdapAuthenticationProvider("ucloud.uc.edu", "ldaps://ucdceiw11.ucloud.uc.edu:636/");
        adProvider.setAuthoritiesMapper(grantedAuthorities());
        adProvider.setUseAuthenticationRequestCredentials(true);
        adProvider.setConvertSubErrorCodesToExceptions(true);
        return adProvider;
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthorities() {
        return new ActiveDirectoryAuthoritiesMapper();
    }

    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new ActiveDirectoryUserContextMapper();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ActiveDirectoryUserDetailsService();
    }
    
    @Autowired DataSource dataSource;
}
