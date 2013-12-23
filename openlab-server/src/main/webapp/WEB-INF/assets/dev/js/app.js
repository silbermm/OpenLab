'use strict';
angular.module('heartbeat', [
   	'templates-main', 
		"ui.router",	
		"ui.router.state", 
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
    growlProvider.globalTimeToLive(8000);    
})
.run(function run($rootScope, $state, $stateParams){
    $rootScope.$state = $state;    
    $rootScope.$stateParams = $stateParams;       
    $state.transitionTo("home");    
})
.controller('AppCtrl', function AppCtrl($scope, $location, titleService, authService){
    //titleService.setTitle("Home");       
     
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        if ( angular.isDefined( toState.data.pageTitle ) ) {
          $scope.pageTitle = toState.data.pageTitle + ' | OpenLab' ;
        }
    });
    
    authService.isAuthenticated().then(function(data){
    	if(data.status === 200){
    		$scope.isAuthenticated = true;
    		$scope.username = data.data.username
    	} else {
    		$scope.isAuthenticated = false;
    	}
    });           
    
});


