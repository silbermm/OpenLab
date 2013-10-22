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

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "heartbeat_authorities")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@authorityId")
public class Authority implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private long authorityId;
    private String authority;
    private Set<WebUser> webUsers = new HashSet<WebUser>(0);
    private Set<Permission> permissions = new HashSet<Permission>(0);
    private Set<MachineGroup> machineGroups = new HashSet<MachineGroup>(0);
    
	public Authority() {
        // must have one default constructor
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "authority_id", nullable = false, unique = true)
    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    public Set<WebUser> getWebUsers() {
        return this.webUsers;
    }

    public void setWebUsers(Set<WebUser> users) {
        this.webUsers = users;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name= "heartbeat_user_permissions", 
            joinColumns={
                @JoinColumn(name="authorityId")}, 
            inverseJoinColumns = {
                @JoinColumn(name="permissionId")
        })
    public Set<Permission> getPermissions(){
    	return permissions;
    }
    
    public void setPermissions(Set<Permission> perms){
    	this.permissions = perms;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    public Set<MachineGroup> getMachineGroups() {
		return machineGroups;
	}

	public void setMachineGroups(Set<MachineGroup> machineGroups) {
		this.machineGroups = machineGroups;
	}
}
