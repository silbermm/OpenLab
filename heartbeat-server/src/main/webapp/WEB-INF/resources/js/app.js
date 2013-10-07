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
])
.config( function myAppConfig($stateProvider, $urlRouterProvider) {      
    //$urlRouterProvider.otherwise("/home");        
})
.run(function run(titleService, $rootScope, $state, $stateParams){
    $rootScope.$state = $state;    
    $rootScope.$stateParams = $stateParams;
    titleService.setSuffix(' | Heartbeat');   
    $state.transitionTo("home");    
})
.controller('AppCtrl', function AppCtrl($scope, $location, titleService){
    titleService.setTitle("Home");   
    //$scope.state = $state;
});






