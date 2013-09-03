'use strict';
angular.module('heartbeat', [
    "ui.state", 
    "ui.route",
    "ngGrid",
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
.controller('AppCtrl', function AppCtrl($scope, $location, titleService, searchService, Restangular){
    titleService.setTitle("HelloWorld");
    var groups = Restangular.one('heartbeat/group', 'all');
    $scope.groups = groups.getList();    
    $scope.searchPlaceholder = searchService.getPlaceholder();
    $scope.optionsItems = [
        {"name" : "filter",
         "display" : "Add a filter"
        }        
    ];    
        
});






