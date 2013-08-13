package edu.uc.labs.heartbeat.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

public class SamlAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    private String mail;
    private String uceduUCID;
    private String telephoneNumber;
    private String displayName;
    private String sn;
    private String givenName;
    
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
        return hsr.getHeader("cn");
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
       return hsr.getHeader("cn");
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUceduUCID() {
        return uceduUCID;
    }

    public void setUceduUCID(String uceduUCID) {
        this.uceduUCID = uceduUCID;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    
    
    
}
