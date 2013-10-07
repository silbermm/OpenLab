'use strict';
angular.module('heartbeat.groups', [
    'ui.state',
    'titleService',
    'restangular',
    'ngGrid',
    'ui.bootstrap'
]).config(function config($stateProvider, $urlRouterProvider) {
    $stateProvider.state('groups', {
        abstract: true,
        url: '/groups/:id',
        views: {
            "main": {
                controller: "GroupsCtrl",
                templateUrl: 'resources/js/groups/groups.tpl.html'
            }            
        }
    }).state('groups.tableview', {
        url: '/table',
        templateUrl: 'resources/js/groups/groups.tableview.tpl.html',
    }).state('groups.gridview', {
        url: '/grid',
        templateUrl: 'resources/js/groups/groups.gridview.tpl.html',
    })
}).controller('GroupsCtrl', function GroupsController($scope, titleService, $dialog, $stateParams, $state,  $http) {    
    $scope.currentGroup = $stateParams.id;
    $http.get('group/' + $stateParams.id).success(function(data, status, headers, config) {
        $scope.group = data;
        $scope.machines = data.machines;
        titleService.setTitle(data.name);
    }).error(function(data, status, headers, config) {
        console.log(data);
    });
    $http({method: 'GET', url: 'group/all', cache: true}).success(function(data, status, headers,config){
        $scope.groups = data;
    }).error(function(data, status, headers,config){
        console.log(data);
    })    
    
    
    
    $scope.optionsItems = [
        {"name" : "filter",
         "display" : "Add a filter"
        }        
    ];
    
    $scope.predicate = 'name';
    $scope.reverse = false;

    $scope.moveModalOpts = {
        backdrop: true,
        keyboard: true,
        backdropClick: false,
        templateUrl: 'resources/js/groups/groups.movemodal.tpl.html',
        controller: 'ModalCtrl'
    };

    $scope.openMoveDialog = function(selectedMachines) {
        var d = $dialog.dialog($scope.moveModalOpts);
        d.open().then(function(group) {
            // Need to search for the correct group...                        
            if (group && group.groupId != $scope.currentGroup){
                //alert('dialog closed with result: ' + group.name);
                actOnSelected(selectedMachines, function(machine){
                   console.log(machine);
                   $http.put("machine/" + machine.uuid + "/to/" + group.groupId).
                   success(function(data,status,headers,config){    
                        console.log("Moved " + machine.name + " to " + group.name);
                   })
                   .error(function(data, status, headers, config){
                        console.log(data);
                   });
                });
            } else {
                console.log("user aborted...");
            }
        });
    }
    

    $scope.machineSelected = {};

    $scope.getUseClass = function(machine) {
        if (machine.currentUser == "null" || machine.currentUser == "" || machine.currentUser == 0) {
            return "mac-available";
        } else {
            return "mac-being-used";
        }
    }

    $scope.isMachineSelected = function() {
        var keys = Object.keys($scope.machineSelected);
        for (var i = 0; i < keys.length; i++) {
            var k = keys[i];
            if ($scope.machineSelected[k]) {
                return true;
            }
        }
        return false;
    }

    $scope.getClassForSortIcon = function(reverse) {
        if (reverse) {
            return "icon-caret-down";
        }
        return "icon-caret-up";
    }

    $scope.showSortArrow = function(currentVal, clickVal) {
        if (currentVal == clickVal) {
            return true;
        }
        return false;
    }

    $scope.selectAll;

    $scope.selectAllMachines = function(selectAll, machines) {
        console.log("selectAll = " + selectAll);
        for (var i = 0; i < machines.length; i++) {
            $scope.machineSelected[machines[i].id] = selectAll;
        }
    }

    $scope.moveSelected = function(selected) {
        actOnSelected(selected, function(machineId) {
            console.log("Acting on machine with id " + id);
        });
    }

    $scope.deleteSelected = function(selected) {
        var keys = Object.keys(selected);
        for (var i = 0; i < keys.length; i++) {
            var k = keys[i];
            if (selected[k]) {
                $http.delete("machine/" + k);
                for (var j = 0; j < $scope.machines.length; j++) {
                    if ($scope.machines[j].id == k) {
                        $scope.machines.splice(j, 1);
                    }
                }
                delete selected[k];
            }
        }
    }

    var actOnSelected = function(selection, callback) {
        var keys = Object.keys(selection);
        for (var i = 0; i < keys.length; i++) {
            var k = keys[i];
            if (selection[k]) {
                callback(k);                
                delete selection[k];
            }
        }
    }

}).controller("ModalCtrl", function ModalController($scope, $http, dialog){
    $scope.apply = function(group) {
        dialog.close(group);
    };
    
    $scope.cancel = function(group) {
        dialog.close(false);
    }
    
    $http.get('group/all').success(function(data, status, headers, config){
       $scope.allGroups = data; 
    }).error(function(data, status, headers, config){
        console.log(data);
    });
})

