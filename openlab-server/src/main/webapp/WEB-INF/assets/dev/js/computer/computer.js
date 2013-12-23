'use strict';
angular.module('heartbeat.computer', [
    'ui.router.state',
    'titleService',
]).config(function config($stateProvider) {
    $stateProvider.state('computer', {
        url: '/computer/:id',
        views: {
            "main": {
                controller: "ComputerCtrl",
                templateUrl: 'computer/computer.tpl.html'
            }
        }
    })
}).controller('ComputerCtrl', function ComputerController($scope, titleService, $stateParams) {
    titleService.setTitle($stateParams.id);
    $scope.computerid = $stateParams.id;
    

})

