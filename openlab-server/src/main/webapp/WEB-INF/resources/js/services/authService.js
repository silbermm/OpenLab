'use strict';

angular.module('authService', []).factory('authService', function($log, $http) {
		
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
		}
	}

});
