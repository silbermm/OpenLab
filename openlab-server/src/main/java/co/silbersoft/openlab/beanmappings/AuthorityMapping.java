package co.silbersoft.openlab.beanmappings;

import java.util.*;

import co.silbersoft.openlab.models.Permission;

public class AuthorityMapping {

	private String authority;
	private long authorityId;
	private Set<Permission> permissions;

	public AuthorityMapping(){}
	
	public AuthorityMapping(long id, String name){
		this.authority = name;
		this.authorityId = id;
	}	
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public long getAuthorityId() {
		return authorityId;
	}
	public void setAuthorityId(long authorityId) {
		this.authorityId = authorityId;
	}
	public Set<Permission> getPermissions(){
		return this.permissions;
	}
	public void setPermissions(Set<Permission> permissions){
		this.permissions = permissions;
	}
}
