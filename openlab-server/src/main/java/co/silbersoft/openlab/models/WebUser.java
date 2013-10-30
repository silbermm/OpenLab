package co.silbersoft.openlab.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "heartbeat_users")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@user_id")
public class WebUser implements Serializable {

	private static final long serialVersionUID = -5818802238516156902L;
	private Long userId;
    private String cn;
    private String dn;
    private String email;
    private boolean enabled = true;    
    private Set<Authority> authorites = new HashSet<Authority>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    @Column
    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
    
    @Column
    public boolean getEnabled() {
    	return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }
    
    @Column(nullable = true)
	public String getDn() {
		return this.dn;		
	}
    
    public void setDn(String dn){
    	this.dn = dn;
    }    
    @Column(nullable=true)
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name= "heartbeat_user_authorites", 
            joinColumns={
                @JoinColumn(name="user_id")}, 
            inverseJoinColumns = {
                @JoinColumn(name="authority_id")
        })
    public Set<Authority> getAuthorities(){
        return this.authorites;
    }
    
    public void setAuthorities(Set<Authority> authorities){
        this.authorites = authorities;
    }
    
    public void addAuthority(Authority a){
    	this.authorites.add(a);
    }
    
    public void removeAllAuthorities(){
    	this.authorites.clear();
    }
			          
}
