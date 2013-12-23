'use strict';
angular.module('heartbeat.home', [
    'ui.router.state',
    'titleService',
]).config(function config($stateProvider) {
    $stateProvider.state('home', {
        url: '/home',
        views: {
            "main": {
                controller: "LoginCtrl",
                templateUrl: 'login/login.tpl.html',
                data:{ pageTitle: 'Login' }
            }
        }
    })
}).controller('LoginCtrl', function LoginController($scope, titleService) {
    

})

