'use strict';
angular.module('heartbeat', [
    "ui.state", 
    "ui.route",
    "ngGrid",
    "ui.bootstrap",
    "heartbeat.home",
    "heartbeat.computer",
    "heartbeat.groups",
    "heartbeat.settings",
    "heartbeat.profile",
    'angular-growl',
    'authService',
])
.config( function myAppConfig($stateProvider, $urlRouterProvider, growlProvider) {      
    growlProvider.globalTimeToLive(5000);    
})
.run(function run(titleService, $rootScope, $state, $stateParams){
    $rootScope.$state = $state;    
    $rootScope.$stateParams = $stateParams;
    titleService.setSuffix(' | OpenLab');   
    $state.transitionTo("home");    
})
.controller('AppCtrl', function AppCtrl($scope, $location, titleService, authService){
    titleService.setTitle("Home");       
    
    
    authService.isAuthenticated().then(function(data){
    	if(data.status === 200){
    		$scope.isAuthenticated = true;
    		$scope.username = data.data.username
    	} else {
    		$scope.isAuthenticated = false;
    	}
    });
           
    
});






