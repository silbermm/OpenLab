'use strict';
angular.module('heartbeat.settings', [
    'ui.state',
    'titleService',
    'ngGrid',
    'ui.bootstrap',
    'authService',
    'angular-growl',
]).config(function config($stateProvider, $urlRouterProvider) {
    $stateProvider.state('settings', {
        abstract: true,
        url: '/settings',
        views: {
            "main": {
                controller: "SettingsCtrl",
                templateUrl: 'resources/js/settings/settings.tpl.html'
            }
        }   
    }).state('settings.users', {
    	url: '/users',
    	templateUrl: 'resources/js/settings/users/settings.users.tpl.html',    	
    }).state('settings.roles', {
    	url: '/roles',
    	templateUrl: 'resources/js/settings/roles/settings.roles.tpl.html',
    }).state('settings.permissions',{
    	url: '/permissions',
    	templateUrl: 'resources/js/settings/permissions/settings.permissions.tpl.html',
    });
})
.controller('SettingsCtrl', function SettingsController($scope,titleService,$stateParams,$state,$http,$log,authService,growl,$modal) {	
	titleService.setTitle("All Settings");	
	authService.isAdmin().then(function(retVal){
		$log.debug(retVal);
		if(!retVal){
			$log.debug("NOT AUTHORIZED!")
			$state.transitionTo("home"); //really need to transition to login
		} else {
			$log.debug("AUTHORIZED!");
		}
	});	
	$scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {		
		// settings.user
		if($state.is("settings.users")){									
			$http.get("accounts/show/users/enabled").success(function(data, status, config, other){
				$scope.users = data;
			}).error(function(data,status,config,other){
				$log.debug(data);
				growl.addErrorMessage("Unable to get enabled users at this time");
			});			
			$http.get("accounts/show/users/disabled").success(function(data, status, config, other){
				$scope.disabledUsers = data;
			}).error(function(data,status,config,other){
				$log.debug(data);
				growl.addErrorMessage("Unable to get disabled users at this time");
			});			
		} else if($state.is("settings.roles")){
			
			
		}										
	});	
	// Enable User Modal
	$scope.enableUser = function(user){
		$http.put("accounts/user/" + user.id + "/enable").success(function(data,status,config,other){
			growl.addSuccessMessage("Successfully enabled " + user.cn);
			angular.forEach($scope.disabledUsers, function(val,idx){
				$log.debug("does " + val.id + " = " + user.id + "?");
				if(val.id === user.id){
					$log.debug("yes - index = " + idx);
					$scope.disabledUsers.splice(idx, 1);
				} else {
					$log.debug("no");
				}
			});
			$scope.users.push(user);
			
		}).error(function(data,status,config,other){
			growl.addErrorMessage("Unable to disable " + user.cn);
		});
	}	
	// Add User Modal
	$scope.openAddUserModal = function(){
		var modalInstance = $modal.open({
			templateUrl: 'resources/js/settings/users/new-user.modal.html',
	        controller: 'NewUserModalCtrl',	        
		});
		modalInstance.result.then(function(obj){
			$http.post("accounts/create", obj).success(function(data,status,config,other){
				$scope.users.push(obj);
				growl.addSuccessMessage("Successfully added user " + obj.cn );
			}).error(function(data,status,config,other){
				growl.addErrorMessage("Unable to add " + obj.cn + ". Please try again later" );
			});						
		});
	}
	$scope.deleteUserModal = function(user){
		if(user.authorities.length > 0){
			growl.addErrorMessage("First remove the all roles from the user!");
			return false;
		} else {				
			var modalInstance = $modal.open({
				templateUrl: 'resources/js/settings/users/delete-user.modal.html',
	        	controller: 'DeleteUserModalCtrl',
	        	resolve: {
	        		items: function() {
	        			return user;
	        		}
	        	}	        
			});
			modalInstance.result.then(function(obj){
				// delete the user...
				$http.delete("accounts/delete/" + obj.id).success(function(data,status,config,other){
					angular.forEach($scope.users, function(val, idx){
						if(val.id == obj.id){
							$scope.users.splice(idx, 1);
						}
					});
				}).error(function(data,status,config,other){
					growl.addErrorMessage("Unable to delete " + obj.cn + " at this time. Try again later.");
				});
			});
		}
	}
	$scope.openRemoveRoleFromUserModal = function(role, user){	
		$log.debug(role);
		$log.debug(user);
		var modalInstance = $modal.open({
			templateUrl: 'resources/js/settings/users/role-remove.modal.html',
	        controller: 'RemoveRoleFromUserModalCtrl',
	        resolve: {
	        	items: function() {
	        		return {
	        			'user':user,
	        			'role':role
	        		}
	        	}
	        }
		});
		modalInstance.result.then(function(obj){
			$http.delete("accounts/role/" + obj.role.authorityId + "/from/" +  obj.user.id).success(function(data,status,config,other){
				var userIdx;
				var roleIdx;
				angular.forEach($scope.users, function(val,idx){
					if(val.id === obj.user.id){
						// this is the correct user...
						userIdx = idx;
						angular.forEach(val.authorities, function(aVal,aIdx){
							if(val.authorityId === obj.role.authorityId){
								roleIdx = aIdx;
							}
						});
					}
				});				
				$scope.users[userIdx].authorities.splice(roleIdx, 1);				
				growl.addSuccessMessage("Successfully removed " + obj.user.cn + " from the " + obj.role.authority + " role");
			}).error(function(data,status,config,other){
				growl.addErrorMessage("Unable to remove " + obj.user.cn + " from the " + obj.role.authority + " at this time");
			});
		});		
	}
	$scope.openDisableUserModal = function(user){
		var modalInstance = $modal.open({
			templateUrl: 'resources/js/settings/users/disable-user.modal.html',
	        controller: 'DisableUserModalCtrl',
	        resolve: {
	        	items: function() {
	        		return user;
	        	}
	        }
		});
		modalInstance.result.then(function(user) {
			$log.debug("trying to disable " + user.cn);
			$http.put("accounts/user/" + user.id + "/disable").success(function(data,status,config,other){
				growl.addInfoMessage("Successfully disabled " + user.cn);
				angular.forEach($scope.users, function(val,idx){
					$log.debug("does " + val.id + " = " + user.id + "?");
					if(val.id === user.id){						
						$scope.users.splice(idx, 1);
					}
				});
				$scope.disabledUsers.push(user);
				
			}).error(function(data,status,config,other){
				growl.addErrorMessage("Unable to disable " + user.cn);
			});
		});			
	}	
	$scope.openAddRoleModal = function(user) {	        
		var modalInstance = $modal.open({
			templateUrl: 'resources/js/settings/users/role-settings.modal.html',
	        controller: 'EditRoleModalCtrl',
	        resolve: {
	        	items: function() {
	        		return user;
	        	}
	        }
		});
		modalInstance.result.then(function(obj) {
			$log.debug(obj.sentRoles);
			$log.debug(obj.user);
			$http.put("accounts/add/roles/" + obj.user.id, obj.sentRoles).success(function(data, status, config, other){
				// the array sent back should be the array of roles added.				
				angular.forEach($scope.users, function(val,idx){
					if(val.id === obj.user.id){
						for(var i=0; i<data.length;i++){
							obj.user.authorities.push(data[i]);
						}
					}
				});
			}).error(function(data, status, config, other){
				$log.error(data);
			});
		});
	 }			
}).controller('EditRoleModalCtrl', function EditRoleModalController($scope, $modalInstance, $http, $log, growl, items) {    
	$scope.user = items;
    $scope.allRoles = new Array();
    $http.get("accounts/show/roles").success(function(data,status,config,other){
    	angular.forEach(data, function(val,idx){
    		if(!inAuthoritiesArray(val.authority, $scope.user.authorities)){
    			$scope.allRoles.push(val);
    		}
    	});
    }).error(function(data,status,config,other){
    	growl.addErrorMessage("Unable to get roles at this time." + data.error);
    	$scope.cancel();
    });    
    $scope.ok = function(sentRoles) {    	
        $modalInstance.close({"user" : $scope.user, "sentRoles": sentRoles});
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };       
    // Utility Functions    
    var inAuthoritiesArray = function(needle, haystack){
    	for(var i=0; i<haystack.length; i++){
    		if(haystack[i].authority === needle){
    			return true;
    		}
    	}
    	return false;
    }    
}).controller('DisableUserModalCtrl', function DeleteUserModalController($scope, $modalInstance, $log, items){
	$scope.user = items;
	
	$scope.ok=function(){
		$modalInstance.close($scope.user);
	};
	$scope.cancel = function(){
		$modalInstance.dismiss('cancel');
	};		
}).controller('RemoveRoleFromUserModalCtrl', function RemoveRoleFromUserModalController($scope, $modalInstance, $log, items){
	$scope.user = items.user;
	$scope.role = items.role;
	$scope.ok=function(){
		$modalInstance.close({'role':$scope.role,'user':$scope.user});
	};
	$scope.cancel = function(){
		$modalInstance.dismiss('cancel');
	};		
}).controller('NewUserModalCtrl', function NewUserModalController($scope, $modalInstance, $log, $http){	
	$scope.search = function(searchText){		
		$http.get("accounts/find?query=" +  searchText).success(function(data,status,config,other){
			$scope.adUsers = data;
		}).error(function(data,status,config,other){
			
		});
	}
	$scope.ok=function(user){		
		$modalInstance.close(user);
	};
	$scope.cancel = function(){
		$modalInstance.dismiss('cancel');
	};	
}).controller('DeleteUserModalCtrl', function DeleteUserModalController($scope, $modalInstance, $log, $http, items){		
	$scope.user = items;	
	$scope.ok=function(){		
		$modalInstance.close($scope.user);
	};
	$scope.cancel = function(){
		$modalInstance.dismiss('cancel');
	};	
})
;