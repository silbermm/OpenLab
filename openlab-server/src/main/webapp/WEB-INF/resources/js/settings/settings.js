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
    }).state('settings.all', {
        url: '/all',
        templateUrl: 'resources/js/settings/all/settings.all.tpl.html',
    }).state('settings.users', {
    	url: '/users',
    	templateUrl: 'resources/js/settings/users/settings.users.tpl.html',    	
    })
})
.controller('SettingsCtrl', function SettingsController($scope, titleService, $stateParams, $state, $http, $log, authService, growl) {
	
	titleService.setTitle("All Settings");
	authService.isAdmin().then(function(retVal){
		if(!retVal){
			$log.debug("NOT AUTHORIZED!")
			$state.transistionTo("home");
		} else {
			$log.debug("AUTHORIZED!");
		}
	});
	
	if($state.is("settings.users")){						
	
		$http.get("accounts/show/users").success(function(data, status, config, other){
			$scope.users = data;
		}).error(function(data,status,config,other){
			$log.debug(data);
			growl.addErrorMessage("Unable to get users");
		});
	
	}
	
	
	
})
;