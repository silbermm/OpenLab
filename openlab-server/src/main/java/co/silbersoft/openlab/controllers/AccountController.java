package co.silbersoft.openlab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.silbersoft.openlab.exceptions.GenericDataException;
import co.silbersoft.openlab.exceptions.NoUserException;
import co.silbersoft.openlab.exceptions.NotAuthenticatedException;
import co.silbersoft.openlab.exceptions.UserExistsException;
import co.silbersoft.openlab.models.Authority;
import co.silbersoft.openlab.models.Failure;
import co.silbersoft.openlab.models.WebUser;
import co.silbersoft.openlab.service.AccountService;


@Controller
@RequestMapping(value="/accounts/")
public class AccountController {
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createAccount(@RequestBody WebUser user){
        accountService.createWebUser(user);
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
    @RequestMapping(value="show/users", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<WebUser> showAllWebUsers(){
    	List<WebUser> users = accountService.getAllWebUsers();
    	for(WebUser u : users){
    		log.debug(u.getCn());
    	}
        return users;
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
    public List<Authority> showAllRoles(){
        return accountService.getAllAuthorities();
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
    public @ResponseBody List<Authority> getUsersRoles(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 	
    	return (List<Authority>) auth.getAuthorities();   	
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
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Failure handleUserExistsException(NoUserException e){
        return new Failure(e.getMessage());
    }
    
    @ExceptionHandler(GenericDataException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Failure handleUserExistsException(GenericDataException e){
        return new Failure(e.getMessage());
    }
    
    @Autowired AccountService accountService;
    
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    
}
