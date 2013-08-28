angular.module('heartbeat.home', [
    'ui.state',
    'titleService',  
])
.config(function config($stateProvider){
    $stateProvider.state('home',{
        url: 'home',
        views: {
            "main" : {
                controller : 'HomeCtrl',
                templateUrl : 'home/home.tpl.html'
            }
        }
    });
})
.controller('HomeCtrl', function HomeController($scope, titleService) {
    titleService.setTitle("Home");
})

