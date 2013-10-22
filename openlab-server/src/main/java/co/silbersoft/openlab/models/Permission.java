package co.silbersoft.openlab.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name = "heartbeat_permission")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@permissionId")
public class Permission implements Serializable {

	private static final long serialVersionUID = -731554837583366993L;
	private long permissionId;
	private String permission;
	private Set<Authority> authorities = new HashSet<Authority>();
	
	public Permission(){}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}
	
	@Column(nullable=false, unique=true)
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
		
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	public Set<Authority> getAuthorities(){
		return authorities;
	}
	public void setAuthorities(Set<Authority> auth){
		this.authorities = auth;
	}
			
}
