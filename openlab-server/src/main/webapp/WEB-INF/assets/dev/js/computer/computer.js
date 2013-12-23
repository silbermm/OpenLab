'use strict';
angular.module('heartbeat.computer', [ 'ui.router.state']).config(function config($stateProvider) {
    $stateProvider.state('computer', {
        url: '/computer/:id',
        views: {
            "main": {
                controller: "ComputerCtrl",
                templateUrl: 'computer/computer.tpl.html',
                data:{ pageTitle: 'Computers' }
            }
        }
    })
}).controller('ComputerCtrl', function ComputerController($scope, $stateParams) {
    $scope.computerid = $stateParams.id;
    

})

