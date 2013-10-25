package co.silbersoft.openlab.beanmappings;

import java.util.ArrayList;
import java.util.List;

import co.silbersoft.openlab.models.Authority;

public class UserMapping {

	public UserMapping(long id, String cn){
		this.id = id;
		this.cn = cn;
		
	}	
	public void addRole(Authority role){
		roles.add(role);
	}
	
	public List<Authority> getAuthorities(){
		return roles;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	} 	
	private long id;
	private String cn;
	private List<Authority> roles = new ArrayList<Authority>();
	private boolean enabled;
	
	
}
