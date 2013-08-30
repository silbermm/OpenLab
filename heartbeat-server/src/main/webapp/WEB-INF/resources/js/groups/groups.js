'use strict';
angular.module('heartbeat.groups', [
    'ui.state',
    'titleService',
    'restangular',
]).config(function config($stateProvider) {
    $stateProvider.state('groups', {
        url: '/groups/:id',
        views: {
            "main": {
                controller: "GroupsCtrl",
                templateUrl: 'resources/js/groups/groups.tpl.html'
            }
        }
    })
}).controller('GroupsCtrl', function GroupsController($scope, titleService, Restangular, $stateParams) {
    var groups = Restangular.one('heartbeat/group', $stateParams.id);
    $scope.group = groups.getList();    
    titleService.setTitle($stateParams.id);
    
    

})

