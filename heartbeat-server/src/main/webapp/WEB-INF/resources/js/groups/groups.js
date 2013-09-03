'use strict';
angular.module('heartbeat.groups', [
    'ui.state',
    'titleService',
    'searchService',
    'restangular',
    'ngGrid'
]).config(function config($stateProvider, $urlRouterProvider) {
    $stateProvider.state('groups', {
        abstract:true,
        url: '/groups/:id',
        views: {
            "main": {
                controller: "GroupsCtrl",
                templateUrl: 'resources/js/groups/groups.tpl.html'
            }
        }
    })
    .state('groups.tableview', {
        url: '/table',
        templateUrl: 'resources/js/groups/groups.tableview.tpl.html',        
    })
}).controller('GroupsCtrl', function GroupsController($scope, titleService, searchService, Restangular, $stateParams) {
    var groups = Restangular.one('heartbeat/group', $stateParams.id);
    $scope.group = groups.getList();    
    $scope.currentGroup = $stateParams.id;
    titleService.setTitle($stateParams.id);        
    
    $scope.getUseClass = function(machine){
        if(machine.currentUser == "null" || machine.currentUser == "" || machine.currentUser == 0){
            return "mac-available";
        } else {
            return "mac-being-used";
        }
    }
    
})

