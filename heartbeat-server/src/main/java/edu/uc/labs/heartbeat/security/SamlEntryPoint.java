package edu.uc.labs.heartbeat.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

public class SamlEntryPoint implements AuthenticationEntryPoint,InitializingBean{

    private String domainLogin;
    
    @Override
    public void commence(HttpServletRequest hsr, HttpServletResponse hsr1, AuthenticationException ae) throws IOException, ServletException {
        String redirectUrl = domainLogin;
        hsr1.sendRedirect(hsr1.encodeRedirectURL(redirectUrl));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(domainLogin, "DomainLogin must be specified");
    }
    
    public String getDomainLogin(){
        return this.domainLogin;
    }
    
    public void setDomainLogin(String domainLogin){
        this.domainLogin = domainLogin;
    }
    
}
