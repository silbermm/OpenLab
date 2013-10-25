package co.silbersoft.openlab.beanmappings;

public class AuthorityMapping {

	private String authority;
	private long authorityId;

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
	
}
