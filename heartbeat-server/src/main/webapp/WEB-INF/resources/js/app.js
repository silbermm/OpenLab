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
    'angular-growl',
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
.controller('AppCtrl', function AppCtrl($scope, $location, titleService){
    titleService.setTitle("Home");       
});






