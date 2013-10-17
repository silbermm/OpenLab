package edu.uc.labs.heartbeat.web;

import edu.uc.labs.heartbeat.exceptions.GenericDataException;
import edu.uc.labs.heartbeat.exceptions.NoUserException;
import edu.uc.labs.heartbeat.exceptions.NotAuthenticatedException;
import edu.uc.labs.heartbeat.exceptions.UserExistsException;
import edu.uc.labs.heartbeat.models.Authority;
import edu.uc.labs.heartbeat.models.Failure;
import edu.uc.labs.heartbeat.models.WebUser;
import edu.uc.labs.heartbeat.service.AccountService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return accountService.getAllWebUsers();
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
    public @ResponseBody Set<Authority> getUsersRoles(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	WebUser user = accountService.getUserByUsername(auth.getName());
    	return user.getAuthorities();
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
    
}