package edu.uc.labs.heartbeat.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "heartbeat_authorities")
public class Authority implements Serializable, GrantedAuthority {

    private long authorityId;
    private String authority;
    private Set<WebUser> webUsers = new HashSet<WebUser>(0);

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
}