package co.silbersoft.openlab.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.silbersoft.openlab.beanmappings.AuthorityMapping;
import co.silbersoft.openlab.beanmappings.UserMapping;
import co.silbersoft.openlab.exceptions.EmptyPermissionException;
import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.exceptions.MethodNotSupportedException;
import co.silbersoft.openlab.exceptions.NoUserException;
import co.silbersoft.openlab.exceptions.NotAuthenticatedException;
import co.silbersoft.openlab.exceptions.UserExistsException;
import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.Failure;
import co.silbersoft.openlab.models.LdapUser;
import co.silbersoft.openlab.models.Permission;
import co.silbersoft.openlab.models.WebUser;
import co.silbersoft.openlab.service.AccountService;


@Controller
@RequestMapping(value="/accounts/")
public class AccountController {

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createAccountFromLdapUser(@RequestBody LdapUser user){
      WebUser u = convertUser(user);
      accountService.createWebUser(u);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="delete/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable long id){
      accountService.deleteWebUser(id);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="update", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateAccount(@RequestBody WebUser user){
        accountService.updateWebUser(user);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="show/users/{enabled}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Collection<UserMapping> showAllWebUsers(@PathVariable String enabled){    
    	List<UserMapping> users = new ArrayList<UserMapping>();    	
    	List<WebUser> foundUsers;
    	if(enabled.equals("enabled")){
    		foundUsers = accountService.getAllEnabledWebUsers();
    	} else if(enabled.equals("disabled")){
    		foundUsers = accountService.getAllDisabledWebUsers();
    		
    	} else {
    		throw new MethodNotSupportedException("show/users/" + enabled + " is not a correct url");
    	}
    	for(WebUser u: foundUsers){
			UserMapping usermap = new UserMapping(u.getUserId(), u.getCn());
			usermap.setEnabled(u.getEnabled());
			for(Authority a: u.getAuthorities()){
				Authority newAuth = new Authority();
				newAuth.setAuthority(a.getAuthority());
				newAuth.setAuthorityId(a.getAuthorityId());					
				usermap.addRole(newAuth);
			}
			users.add(usermap);
		}
        return users;
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="show/user/{userId}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WebUser showUserInfo(@PathVariable long userId){
    	return accountService.findUserById(userId);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="show/users/with/role/{role}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<WebUser> showWebUsersWithRole(@PathVariable long role){
        return accountService.getWebUsersWith(role);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="show/roles", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<AuthorityMapping> showAllRoles(){    	
    	List<AuthorityMapping> rAuth = new ArrayList<AuthorityMapping>();
    	for(Authority a : accountService.getAllAuthorities()){    		
    		AuthorityMapping am = new AuthorityMapping(a.getAuthorityId(), a.getAuthority());
    		if(a.getPermissions() != null || !a.getPermissions().isEmpty()){
    			am.setPermissions(a.getPermissions());
    		}
    		rAuth.add(new AuthorityMapping(a.getAuthorityId(), a.getAuthority()));
    	}
        return rAuth;
    }
        
    @RequestMapping(value="username", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<String,String> getLoggedInUsername() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(!auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
    		throw new NotAuthenticatedException("Not Authenticated");
    	} else {
    		Map<String,String> response = new HashMap();
    		response.put("username", auth.getName());
    		return response;
    	}
    }
           
	@PreAuthorize("isAuthenticated()")
    @RequestMapping(value="roles", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<GrantedAuthority> getUsersRoles(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
    	List<GrantedAuthority> auths =  (List<GrantedAuthority>) auth.getAuthorities();    	
    	return (List<GrantedAuthority>) auth.getAuthorities();   	
    }
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="add/roles/{userId}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<AuthorityMapping> addRolesToUser(@RequestBody List<AuthorityMapping> auths, @PathVariable long userId){
		// update user with new Auths
		log.debug("Trying to add " + auths + " to " + userId);
		List<AuthorityMapping> returnList = new ArrayList<AuthorityMapping>();
		for(AuthorityMapping am : auths){
			if(accountService.addAuthToUser(am.getAuthorityId(), userId)){
				returnList.add(am);
			}
		}
		return returnList;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="role/{roleId}/from/{userId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeRoleFromUser(@PathVariable long roleId, @PathVariable long userId){
		accountService.removeAuthFromUser(roleId, userId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="user/{userid}/{action}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void changeUserStatus(@PathVariable long userid, @PathVariable String action){
		if( action.equals("enable")){
			accountService.enableUser(userid, true);
		} else if(action.equals("disable")) {	
			accountService.enableUser(userid, false);
		} else {
			throw new MethodNotSupportedException("Unable to execute for the method " + action);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="role/{roleId}/permissions", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Set<Permission> getPermissionsForRole(@PathVariable long roleId){
		return accountService.findPermissionsForRole(roleId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="permissions", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Permission> getPermissions(){
		return accountService.findPermissions();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="permissions/role/{roleId}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Permission> addPermissionsToRole(@RequestBody List<Permission> permissions, @PathVariable long roleId){
		List<Permission> successPerm = new ArrayList<Permission>();
		for(Permission p : permissions){
			boolean isAdded = accountService.addPermissionToRole(p.getPermissionId(), roleId);
			if(isAdded) successPerm.add(p);
		}	
		if(successPerm.isEmpty()){
			throw new EmptyPermissionException("Unable to add permissions to role with id " + roleId);
		}
		return successPerm;
	}
	
		
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@RequestMapping(value="find", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<LdapUser> findUser(@RequestParam("query") String query){		
		return accountService.searchForUser(query);		
	}	
    
	@ExceptionHandler(EmptyPermissionException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Failure handleEmptyPermissionException(EmptyPermissionException e){
		return new Failure(e.getMessage());
	}
	
    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Failure handleNotAuthenticatedException(NotAuthenticatedException e){
    	return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Failure handleAuthorizeException(AccessDeniedException e){
        return new Failure("Access Denied");
    }
    
    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Failure handleUserExistsException(UserExistsException e){
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(NoUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleUserExistsException(NoUserException e){
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Failure handleUserExistsException(GenericDataException e){
    	log.debug("Generic Data Exception");
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(MethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleMethodNotSupportedException(MethodNotSupportedException e){
    	return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(NameNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Failure handleNameNotFoundException(NameNotFoundException e){
    	return new Failure(e.getMessage());
    }
    
    private WebUser convertUser(LdapUser u){
    	WebUser user = new WebUser();
    	user.setCn(u.getCn());
    	user.setEmail(u.getEmail());
    	user.setEnabled(true);
    	Set<Authority> auths = new HashSet<Authority>();
    	user.setAuthorities(auths);
    	return user;
    }
    
    @Autowired AccountService accountService;
    
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    
}
