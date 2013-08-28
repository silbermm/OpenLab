'use strict';
angular.module('heartbeat', [
    "ui.state", 
    "heartbeat.home"
])
.config(function myAppConfig($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/home");
})
.run(function run(titleService){
    titleService.setSuffix(' | Heartbeat');
})
.controller('AppCtrl', function AppCtrl($scope, $location, titleService){
    titleService.setTitle("HelloWorld");
});






