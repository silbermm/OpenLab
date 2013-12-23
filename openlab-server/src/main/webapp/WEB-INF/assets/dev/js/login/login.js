'use strict';
angular.module('heartbeat.home', [
    'ui.router.state',
    'titleService',
]).config(function config($stateProvider) {
    $stateProvider.state('home', {
        url: '/home',
        views: {
            "main": {
                controller: "HomeCtrl",
                templateUrl: 'home/home.tpl.html'
            }
        }
    })
}).controller('HomeCtrl', function HomeController($scope, titleService) {
    

})

