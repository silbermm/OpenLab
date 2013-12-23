'use strict';

angular.module('authService', []).factory('authService', function($log, $http) {
		
	var adminRole = "ROLE_ADMINISTRATOR";
	
	return {
		getRoles : function(){
			var promise = $http.get('accounts/roles').then(function(d){
				return d;
			});
			return promise;
		},
		isAuthenticated : function(){			
			var promise = $http.get('accounts/username').then(function(d){
				return d;
			});			
			return promise;
		},
		isAdmin : function() {
			var promise = this.getRoles().then(function(d){
				if(d.status === 200){
					var admin = false;
					angular.forEach(d.data, function(val,idx){						
						if(val.authority === adminRole){ 
							admin = true; 
						}
					});					
					return admin;
				} else {
					return false;
				}
			});
			return promise;
		}
	}
});
