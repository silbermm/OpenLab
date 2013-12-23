'use strict';
angular.module('heartbeat.groups', [
    'ui.router.state',
    'ngGrid',
    'ui.bootstrap',
    'angular-growl',
]).config(function config($stateProvider, $urlRouterProvider) {
    $stateProvider.state('groups', {
        abstract: true,
        url: '/groups/:id',
        views: {
            "main": {
                controller: "GroupsCtrl",
                templateUrl: 'groups/groups.tpl.html'
            }
        }
    }).state('groups.tableview', {
        url: '/table',
        templateUrl: 'groups/groups.tableview.tpl.html',
        data:{ pageTitle: 'Groups Table View' }
    }).state('groups.gridview', {
        url: '/grid',
        templateUrl: 'groups/groups.gridview.tpl.html',
        data:{ pageTitle: 'Groups Grid View' }
    });
}).controller('GroupsCtrl', function GroupsController($scope, $modal, $stateParams, $state, $http, $log, growl) {
    $scope.currentGroup = $stateParams.id;
    $scope.machineSelected = {};
    $scope.selectAll;
    $http.get('group/' + $stateParams.id).success(function(data, status, headers, config) {
        if ($stateParams.id === 'all') {
            $scope.group = "all";
            for (var i = 0; i < data.length; i++) {
                if (i === 0) {
                    $scope.machines = data[i].machines;
                } else {
                    $scope.machines = $scope.machines.concat(data[i].machines);
                }
            }
        } else {
            $scope.group = data;
            $scope.machines = data.machines;
        }
    }).error(function(data, status, headers, config) {
        growl.addErrorMessage("Unable to get the Machines in the " + $stateParams.id + " group. " + data.error);
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
                    };
                }
            }
        });
        modalInstance.result.then(function(obj) {
            if (obj.to && obj.to.groupId !== $scope.currentGroup) {
                actOnSelected(obj.machines, function(machine) {
                    $log.debug(machine);
                    $http.put("machine/id/" + machine + "/to/" + obj.to.groupId).success(function(data, status, headers, config) {
                        angular.forEach($scope.machines, function(val, key) {
                            if (machine == val.id) {
                                $log.info(" YES ");
                                $scope.machines.splice(key, 1);
                            }
                        });
                    });
                });
            }
            ;        
        });
    };

    $scope.openNewGroupDialog = function() {
        var modalInstance = $modal.open({
            templateUrl: 'addGroupModal.html',
            controller: 'GroupModalCtrl',
            resolve: {
                items: function() {
                    return {name: null,description: null};
                }
            }
        });
        modalInstance.result.then(function(group) {
            $http.post("group/", group).success(function(data, status, headers, config) {
                group.groupId = data.created;
                $scope.groups.push(group);
                growl.addSuccessMessage("The group " + group.name + " was created successfully");
            }).error(function(data, status, headers, config) {
                growl.addErrorMessage("Something went horrible wrong! " + data.error);
            });        
        });
    };

    $scope.openEditGroupDialog = function(currentGroupName) {        
        if ($scope.group !== 'all') {            
            var modalInstance = $modal.open({
                templateUrl: 'editGroupModal.html',
                controller: 'GroupModalCtrl',
                resolve: {
                    items: function() {
                        return { 'group': $scope.group, 'oldGroup': $scope.group.name };
                    }
                }
            });
            modalInstance.result.then(function(items) {
                $log.debug("current group name = " + $scope.group.name);
                $log.debug("old group name = " + items.oldGroup);
                $http.put("group/", items.group).success(function(data, status, headers, config) {
                    angular.forEach($scope.groups, function(val, key) {
                        if ($scope.groups[key].name === items.oldGroup) {
                            $scope.groups[key] = items.group;
                            return;
                        }
                    });                    
                    growl.addSuccessMessage("The group " + items.group.name + " was updated successfully");
                }).error(function(data, status, headers, config) {
                    growl.addErrorMessage("Something went horrible wrong! " + data.error);
                });            
            });
        }
    };

    $scope.openDeleteGroupDialog = function() {
        var modalInstance = $modal.open({
            templateUrl: 'resources/js/groups/groups.deletemodal.tpl.html',
            controller: 'GroupModalCtrl',
            resolve: {
                items: function() {
                    return { group : $scope.group, oldGroup: null };                       
                }
            }
        });
        
        modalInstance.result.then(function(items) {
            
        }, function() {
            
        });
    };    

    $scope.getUseClass = function(machine) {
        if (machine.currentUser === "null" || machine.currentUser === "" || machine.currentUser == 0 || machine.currentUser === 'None') {
            return "mac-available";
        } else {
            return "mac-being-used";
        }
    };
    $scope.isMachineSelected = function() {
        var keys = Object.keys($scope.machineSelected);
        for (var i = 0; i < keys.length; i++) {
            var k = keys[i];
            if ($scope.machineSelected[k]) {
                return true;
            }
        }
        return false;
    };
    $scope.getClassForSortIcon = function(reverse) {
        if (reverse) {
            return "icon-caret-down";
        }
        return "icon-caret-up";
    };
    $scope.showSortArrow = function(currentVal, clickVal) {
        if (currentVal == clickVal) {
            return true;
        }
        return false;
    };
    $scope.selectAllMachines = function(selectAll, machines) {
        console.log("selectAll = " + selectAll);
        for (var i = 0; i < machines.length; i++) {
            $scope.machineSelected[machines[i].id] = selectAll;
        }
    };
    $scope.moveSelected = function(selected) {
        actOnSelected(selected, function(machineId) {
            console.log("Acting on machine with id " + id);
        });
    };
    $scope.deleteSelected = function(selected) {
        var keys = Object.keys(selected);
        for (var i = 0; i < keys.length; i++) {
        	var k = keys[i];
            if (selected[k]) {
                $http.delete("machine/" + k).success(function(data,status,config,other){                	                
                	for (var j = 0; j < $scope.machines.length; j++) {
                		if ($scope.machines[j].id == k) {
                			$scope.machines.splice(j, 1);
                		};
                	}
                	delete selected[k];                               
                });
            };
        };
    }

    var actOnSelected = function(selection, callback) {
        var keys = Object.keys(selection);
        for (var i = 0; i < keys.length; i++) {
            var k = keys[i];
            if (selection[k]) {
                callback(k);
                delete selection[k];
            };
        };
    };
}).controller('ModalInstanceCtrl', function ModalInstanceController($scope, $modalInstance, items) {
    $scope.obj = items;
    $scope.ok = function() {
        $modalInstance.close($scope.obj);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}).controller("GroupModalCtrl", function GroupModalController($scope, $modalInstance, items, $log) {
    $scope.group = items.group;
    $scope.oldGroup = items.oldGroup;

    $scope.ok = function() {
        $log.debug($scope.group, $scope.oldGroup);
        $modalInstance.close({group: $scope.group, oldGroup: $scope.oldGroup});
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
})
;

