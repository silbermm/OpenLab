package co.silbersoft.openlab.dao;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;

import co.silbersoft.openlab.models.LdapUser;

public class LdapDaoImpl implements LdapDao {

	public LdapDaoImpl(){
		
	}
	
	@Override
	public List<LdapUser> findByUsername(String username) {
		AndFilter filter = new AndFilter();
	    filter.and(new EqualsFilter("objectclass", "person"));
	    filter.and(new LikeFilter("cn", username+"*"));
	    return ldapTemplate.search("", filter.encode(), new LdapUserMapper());
	}

	@Override
	public List<LdapUser> findByEmployeeId(String id) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new LikeFilter("uceduUCID", id+"*"));
		return ldapTemplate.search("", filter.encode(), new LdapUserMapper());
	}
	
	private class LdapUserMapper implements AttributesMapper {
		@Override
		public Object mapFromAttributes(Attributes attr) throws NamingException {
			LdapUser u = new LdapUser();
			u.setCn((String) attr.get("cn").get());
			u.setFullName((String) attr.get("givenName").get() + " " + (String)attr.get("sn").get());
			u.setMnumber((String) attr.get("uceduUCID").get());
			u.setEmail((String) attr.get("mail").get());
			return u;
		}		
	}

	@Autowired private LdapTemplate ldapTemplate;
		
}
