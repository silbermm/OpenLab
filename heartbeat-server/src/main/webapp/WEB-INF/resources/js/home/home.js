'use strict';
angular.module('heartbeat.home', [
    'ui.state',
    'titleService',
    'restangular',
]).config(function config($stateProvider) {
    $stateProvider.state('home', {
        url: '/home',
        views: {
            "main": {
                controller: "HomeCtrl",
                templateUrl: 'resources/js/home/home.tpl.html'
            }
        }
    })
}).controller('HomeCtrl', function HomeController($scope, titleService, Restangular) {
    

})

