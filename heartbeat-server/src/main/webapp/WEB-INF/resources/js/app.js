'use strict';
angular.module('heartbeat', [
    "ui.state", 
    "ui.route",
    "ui.bootstrap",
    "heartbeat.home",
    "heartbeat.computer",
    "heartbeat.groups",
    "restangular"
])
.config( function myAppConfig($stateProvider, $urlRouterProvider) {    
    $urlRouterProvider.otherwise("/home");        
})
.run(function run(titleService){
    titleService.setSuffix(' | Heartbeat');
})
.controller('AppCtrl', function AppCtrl($scope, $location, titleService, Restangular){
    titleService.setTitle("HelloWorld");
    var groups = Restangular.one('heartbeat/group', 'all');
    $scope.groups = groups.getList();
    
    $scope.optionsItems = [
        {"name" : "filter",
         "display" : "Add a filter"
        }        
    ];    
        
});






