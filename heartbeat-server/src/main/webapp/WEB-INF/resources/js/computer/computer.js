'use strict';
angular.module('heartbeat.computer', [
    'ui.state',
    'titleService',
]).config(function config($stateProvider) {
    $stateProvider.state('computer', {
        url: '/computer/:id',
        views: {
            "main": {
                controller: "ComputerCtrl",
                templateUrl: 'resources/js/computer/computer.tpl.html'
            }
        }
    })
}).controller('ComputerCtrl', function ComputerController($scope, titleService, $stateParams) {
    titleService.setTitle($stateParams.id);
    $scope.computerid = $stateParams.id;
    

})

