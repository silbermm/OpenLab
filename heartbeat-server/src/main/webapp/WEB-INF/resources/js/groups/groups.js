'use strict';
angular.module('heartbeat.groups', [
    'ui.state',
    'titleService',
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
}).controller('GroupsCtrl', function GroupsController($scope, titleService, $modal, $stateParams, $state, $http, $log) {
    $scope.currentGroup = $stateParams.id;

    $http.get('group/' + $stateParams.id).success(function(data, status, headers, config) {
        if($stateParams.id === 'all'){
            $scope.group = "all";            
            for(var i=0;i<data.length;i++){
                if(i===0){
                    $scope.machines = data[i].machines;                
                }else {
                    $scope.machines = $scope.machines.concat(data[i].machines);
                }                    
            }           
            titleService.setTitle('All Machines');
        } else {
            $scope.group = data;
            $scope.machines = data.machines;
            titleService.setTitle(data.name);
        }        
    }).error(function(data, status, headers, config) {
        $log.error(data);
    });

    $http({method: 'GET', url: 'group/all', cache: true}).success(function(data, status, headers, config) {
        $scope.groups = data;
    }).error(function(data, status, headers, config) {
        $log.error(data);
    });

    $scope.optionsItems = [
        {"name": "filter",
            "display": "Add a filter"
        }
    ];

    $scope.predicate = 'name';
    $scope.reverse = false;

    $scope.openMoveDialog = function(selectedMachines) {
        var modalInstance = $modal.open({
            templateUrl: 'moveModal.html',
            controller: 'ModalInstanceCtrl',
            resolve: {
                items: function() {
                    return {
                        machines: selectedMachines,
                        from: $scope.currentGroup,
                        to: null,
                        all: $scope.groups,
                    }
                }
            }
        });

        modalInstance.result.then(function(obj) {            
            $log.info(obj.machines);
            $log.info(obj.from);
            $log.info(obj.to);
            if (obj.to && obj.to.groupId !== $scope.currentGroup) {
                actOnSelected(obj.machines, function(machine) {                    
                    $log.debug(machine);
                    $http.put("machine/id/" + machine + "/to/" + obj.to.groupId).success(function(data, status, headers, config) {                                                                                                                      
                        angular.forEach($scope.machines, function(val,key){                           
                            if(machine == val.id){
                                $log.info(" YES ");
                                $scope.machines.splice(key,1);   
                            }
                        });                        
                    }).error(function(data, status, headers, config) {
                        $log.error("having trouble updating the machine")
                        $log.error(data);
                    });
                });
            }
            ;
        }, function() {
            $log.info('Modal dismissed at: ' + new Date());
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
}).controller('ModalInstanceCtrl', function ModalInstanceController($scope, $modalInstance, items) {

    $scope.obj = items;
   
    $scope.ok = function() {
        $modalInstance.close($scope.obj);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

})
        ;

