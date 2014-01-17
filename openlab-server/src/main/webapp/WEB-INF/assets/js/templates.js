angular.module('templates-main', ['computer/computer.tpl.html', 'groups/groups.deletemodal.tpl.html', 'groups/groups.gridview.tpl.html', 'groups/groups.movemodal.tpl.html', 'groups/groups.tableview.tpl.html', 'groups/groups.tpl.html', 'home/home.tpl.html', 'login/login.tpl.html', 'profile/profile.tpl.html', 'settings/all/settings.all.tpl.html', 'settings/permissions/settings.permissions.tpl.html', 'settings/roles/settings.roles.tpl.html', 'settings/settings.tpl.html', 'settings/users/settings.users.tpl.html']);

angular.module("computer/computer.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("computer/computer.tpl.html",
    "<h1>HEllo Computer View</h1><h2>{{ computerid }}</h2>");
}]);

angular.module("groups/groups.deletemodal.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("groups/groups.deletemodal.tpl.html",
    "<div class=modal-header><h3>Delete Group {{group.name}}</h3></div><div class=modal-body><form name=deleteGroupForm class=\"form-horizontal css-form\" novalidate><div class=control-group><label class=control-label for=groupName>Group Name:</label><div class=controls><input ng-model=group.name id=groupName required min=1/></div></div><div class=control-group><label class=control-label for=groupDesc>Group Description:</label><div class=controls><input ng-model=group.description id=groupDesc required min=1></div></div></form></div><div class=modal-footer><button class=\"btn btn-primary\" ng-disabled=editGroupForm.$invalid ng-click=ok()>OK</button> <button class=\"btn btn-warning\" ng-click=cancel()>Cancel</button></div>");
}]);

angular.module("groups/groups.gridview.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("groups/groups.gridview.tpl.html",
    "<h2>Grid View</h2>");
}]);

angular.module("groups/groups.movemodal.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("groups/groups.movemodal.tpl.html",
    "<div class=modal-header><h3>Move computers</h3></div><div class=modal-body><p>Where do you want to move the selected computers to?</p><select ng-model=chosenGroup ng-options=\"g.name for g in allGroups\"></select></div><div class=modal-footer><button ng-click=apply(chosenGroup) class=\"btn btn-primary\">Move</button></div>");
}]);

angular.module("groups/groups.tableview.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("groups/groups.tableview.tpl.html",
    "<table class=\"table table-condensed machine-table\" style=width:100%><thead><tr><th><input type=checkbox ng-click=\"selectAllMachines(selectAll, group.machines)\" ng-model=selectAll></th><th><a ng-click=\"predicate='name'; reverse=!reverse\">Name</a> <i ng-class=getClassForSortIcon(reverse) ng-if=\"predicate == 'name'\"></i></th><th>OS</th><th>Current User</th><th>Version</th><th>Serial Number</th><th>IP Address</th><th>Manufacturer / Model</th><th><a ng-click=\"predicate='lastSeen'; reverse=!reverse\">Last Seen</a> <i ng-class=getClassForSortIcon(reverse) ng-if=\"predicate == 'lastSeen'\"></i></th><th></th></tr></thead><tbody><tr ng-repeat=\"machine in machines | filter:search | orderBy:predicate:reverse\"><td><input type=checkbox ng-model=machineSelected[machine.id] id={{machine.id}} value={{machine.id}}></td><td ng-if=\"machine.os=='Windows 7'\">{{machine.name }}</td><td ng-if=\"machine.os!='Windows 7'\">{{machine.macName }}</td><td ng-if=\"machine.os=='Windows 7'\"><i class=\"fa fa-windows larger-font\" ng-class=getUseClass(machine)></i></td><td ng-if=\"machine.os=='Mac OS X'\"><i class=\"fa fa-apple larger-font\" ng-class=getUseClass(machine)></i></td><td>{{machine.currentUser}}</td><td>{{machine.osVersion}}</td><td>{{machine.serialNumber}}</td><td>{{machine.ipAddress}}</td><td>{{machine.manufacturer}} / {{machine.model}}</td><td>{{machine.lastSeen | date:'short'}}</td><td></td></tr></tbody></table>");
}]);

angular.module("groups/groups.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("groups/groups.tpl.html",
    "<nav id=group-sidebar class=col-md-3><h4 class=dashboard style=height:2%><a href=#><i class=\"fa fa-home\"></i> Groups</a></h4><div id=groups-overflow><ul class=groups ng-model=chosenGroup><li class=closed ui-route=/groups/all/:view ng-class=\"{active:$state.params.id == 'all'}\"><span class=toggle><i class=\"fa fa-caret-right\"></i></span><h4 class=groupName><a href=#/groups/all/table>All</a></h4><span class=loader>Loading</span><div class=\"options dropdown\"><a dropdown-toggle=\"\"><i class=\"fa fa-filter\"></i></a><ul class=\"dropdown-menu groups-dropdown-menu\"><li ng-repeat=\"choice in optionsItems\"><a>{{choice.display}}</a></li></ul></div></li><li class=closed ng-repeat=\"group in groups\" ng-class=\"{active:$state.params.id == group.groupId}\"><span class=toggle><i class=\"fa fa-caret-right\"></i></span><h4 class=groupName><a href=#/groups/{{group.groupId}}/table ng-model=group.name>{{group.name}}</a></h4><span class=loader>Loading</span><div class=\"options dropdown\"><a dropdown-toggle=\"\"><i class=\"fa fa-filter\"></i></a><ul class=\"dropdown-menu groups-dropdown-menu\"><li ng-repeat=\"choice in optionsItems\"><a>{{choice.display}}</a></li></ul></div></li></ul></div><div class=\"grp-controls container\"><ul class=list-inline><li><a popover-placement=right popover=\"Add a new Group\" popover-trigger=mouseenter ng-click=openNewGroupDialog()><i class=\"fa fa-plus\"></i></a></li><li><a popover-placement=right popover=\"Edit selected Group\" popover-trigger=mouseenter ng-click=openEditGroupDialog()><i class=\"fa fa-edit\"></i></a></li><li><a popover-placement=right popover=\"Remove selected Group\" popover-trigger=mouseenter><i class=\"fa fa-ban text-danger\"></i></a></li></ul></div></nav><div id=ui-view-main><div class=row><header><div class=pull-left><div class=btn-group><button type=button class=\"btn btn-default dropdown-toggle\" data-toggle=dropdown href=#><i class=\"fa fa-tasks\"></i> Actions <span class=caret></span></button><ul class=dropdown-menu><li ng-class=\"{disabled: !isMachineSelected()}\"><a ng-click=openMoveDialog(machineSelected)><i class=\"fa fa-move\"></i>Move</a></li><li ng-class=\"{disabled: !isMachineSelected()}\"><a><i class=\"fa fa-fixed-width fa-power-off\"></i>Power</a></li><li ng-class=\"{disabled: !isMachineSelected()}\"><a ng-click=deleteSelected(machineSelected)><i class=\"fa fa-trash-o\"></i>Delete</a></li></ul></div><input class=form-search ng-model=search id=search placeholder=Search></div><div class=pull-right><ul class=\"nav nav-pills\"><li ui-route=/groups/{{currentGroup}}/table ng-class={active:$uiRoute}><a type=button class=\"btn btn-default dropdown-toggle\" href=#/groups/{{currentGroup}}/table><i class=\"fa fa-list\"></i> Table View</a></li><li ui-route=/groups/{{currentGroup}}/grid ng-class={active:$uiRoute}><a type=button class=\"btn btn-default dropdown-toggle\" href=#/groups/{{currentGroup}}/grid><i class=\"fa fa-th\"></i> Grid View</a></li></ul></div></header></div><div id=main-content-right class=row style=\"top: 59px\"><div ui-view=\"\"></div></div></div><script type=text/ng-template id=moveModal.html><div class=\"modal-header\">\n" +
    "    <h3>Move Computers</h3>\n" +
    "    </div>\n" +
    "    <div class=\"modal-body\">\n" +
    "    <p> Where would you like to move the computer(s) too? </p>\n" +
    "    <form class=\"form-horizontal css-form\"> \n" +
    "    <div class=\"control-group\">\n" +
    "    <label class=\"control-label\" for=\"groupSelect\">All Groups: </label>\n" +
    "    <div class=\"controls\">\n" +
    "    <select ng-model=\"obj.to\" ng-options=\"g.name for g in obj.all\"></select>\n" +
    "    </div>\n" +
    "    </div>        \n" +
    "    </form>      \n" +
    "    </div>\n" +
    "    <div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-primary\" ng-disabled=\"form.$invalid\" ng-click=\"ok()\">OK</button>\n" +
    "    <button class=\"btn btn-warning\" ng-click=\"cancel()\">Cancel</button>\n" +
    "    </div></script><script type=text/ng-template id=addGroupModal.html><div class=\"modal-header\">\n" +
    "    <h3>Add a Group</h3>\n" +
    "    </div>\n" +
    "    <div class=\"modal-body\">    \n" +
    "    <form name=\"addGroupForm\" class=\"form-horizontal css-form\" novalidate> \n" +
    "    <div class=\"control-group\">\n" +
    "    <label class=\"control-label\" for=\"groupName\">Group Name: </label>\n" +
    "    <div class=\"controls\">\n" +
    "    <input type=\"text\" ng-model=\"group.name\" id=\"groupName\" required min=1/>  \n" +
    "    </div>            \n" +
    "    </div>\n" +
    "    <div class=\"control-group\">\n" +
    "    <label class=\"control-label\" for=\"groupDesc\">Group Description: </label>\n" +
    "    <div class=\"controls\">\n" +
    "    <input type=\"text\" ng-model=\"group.description\" id=\"groupDesc\" required min=1 >\n" +
    "    </div>            \n" +
    "    </div>\n" +
    "    </form>   \n" +
    "    </div>\n" +
    "    <div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-primary\" ng-disabled=\"addGroupForm.$invalid\" ng-click=\"ok()\">OK</button>\n" +
    "    <button class=\"btn btn-warning\" ng-click=\"cancel()\">Cancel</button>\n" +
    "    </div></script><script type=text/ng-template id=editGroupModal.html><div class=\"modal-header\">\n" +
    "    <h3>Edit Group {{group.name}} </h3>\n" +
    "    </div>\n" +
    "    <div class=\"modal-body\">    \n" +
    "    <form name=\"editGroupForm\" class=\"form-horizontal css-form\" novalidate> \n" +
    "    <div class=\"control-group\">\n" +
    "    <label class=\"control-label\" for=\"groupName\">Group Name: </label>\n" +
    "    <div class=\"controls\">\n" +
    "    <input type=\"text\" ng-model=\"group.name\" id=\"groupName\" required min=1/>  \n" +
    "    </div>            \n" +
    "    </div>\n" +
    "    <div class=\"control-group\">\n" +
    "    <label class=\"control-label\" for=\"groupDesc\">Group Description: </label>\n" +
    "    <div class=\"controls\">\n" +
    "    <input type=\"text\" ng-model=\"group.description\" id=\"groupDesc\" required min=1 >\n" +
    "    </div>            \n" +
    "    </div>\n" +
    "    </form>   \n" +
    "    </div>\n" +
    "    <div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-primary\" ng-disabled=\"editGroupForm.$invalid\" ng-click=\"ok()\">OK</button>\n" +
    "    <button class=\"btn btn-warning\" ng-click=\"cancel()\">Cancel</button>\n" +
    "    </div></script>");
}]);

angular.module("home/home.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("home/home.tpl.html",
    "");
}]);

angular.module("login/login.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("login/login.tpl.html",
    "");
}]);

angular.module("profile/profile.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("profile/profile.tpl.html",
    "<div class=container><h2>Roles for {{username}}</h2><ul class=well><li ng-repeat=\"role in roles\">{{role.authority}}</li></ul></div>");
}]);

angular.module("settings/all/settings.all.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("settings/all/settings.all.tpl.html",
    "");
}]);

angular.module("settings/permissions/settings.permissions.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("settings/permissions/settings.permissions.tpl.html",
    "");
}]);

angular.module("settings/roles/settings.roles.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("settings/roles/settings.roles.tpl.html",
    "<div class=container><div class=row-fluid><div class=\"span4 well\"><ul class=\"nav nav-list\"><li class=nav-header>Roles</li><li ng-repeat=\"role in roles\" ng-class=\"{active : role.authority == currentrole.authority}\"><a ng-click=setCurrentRole(role)>{{role.authority}}</a></li><li class=divider></li><li><a><i class=\"fa fa-plus\"></i> Add a new Role</a></li></ul></div><div class=\"span4 well\" ng-if=currentrole><ul class=\"nav nav-list\"><li class=nav-header>Permissions for {{currentrole.authority}}</li><li ng-repeat=\"perm in permissions\" ng-class=\"{active:currentpermission == perm.permission}\">{{perm.permission}} <span class=pull-right><a ng-click=deletePermissionModal()><i class=\"fa fa-minus\"></i></a></span></li><li class=divider></li><li><a ng-click=addPermissionModal()><i class=\"fa fa-plus\"></i> Add Permissions</a></li></ul></div></div></div>");
}]);

angular.module("settings/settings.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("settings/settings.tpl.html",
    "<nav id=group-sidebar class=span3><h4 class=dashboard style=\"height: 2%\"><a><i class=icon-gears></i> All Settings</a></h4><div id=groups-overflow><ul class=settings><li ui-route=/settings/users ng-class=\"{active: $state.includes('settings.users')}\"><span class=toggle></span><h4 class=groupName><i class=icon-user></i><a href=#/settings/users>USERS</a></h4></li></ul><ul class=settings><li ui-route=/settings/roles ng-class=\"{active: $state.includes('settings.roles')}\"><span class=toggle></span><h4 class=groupName><i class=icon-group></i><a href=#/settings/roles>ROLES</a></h4></li></ul><ul class=settings><li ui-route=/settings/ ng-class=\"{active: $state.includes('settings.permissions')}\"><span class=toggle></span><h4 class=groupName><i class=icon-lock></i><a href=#/settings/permissions>PERMISSIONS</a></h4></li></ul></div></nav><div id=ui-view-main class=settings-ui><div class=row-fluid><header><div class=pull-left></div><div class=pull-right><ul class=\"nav nav-pills\"></ul></div></header></div><div id=main-content-right class=row-fluid style=\"top: 59px\"><div ui-view=\"\"></div></div></div>");
}]);

angular.module("settings/users/settings.users.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("settings/users/settings.users.tpl.html",
    "<div class=container><div class=row-fluid><div class=widget><div class=widget-header><i class=icon-ok style=color:green></i><h3>Enabled Users <span class=\"badge badge-important\">{{users.length}}</span></h3><form class=form-search style=display:inline><input class=search-query ng-model=searchEnabled id=searchEnabled placeholder=Search></form><button class=\"btn pull-right\" ng-click=openAddUserModal()><i class=icon-plus></i> Add User</button></div><div class=widget-content><table class=\"widget-table table table-striped\"><thead><tr><th>UserId</th><th>Username</th><th>Roles</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"user in users | filter:searchEnabled\"><td>{{ user.id }}</td><td>{{ user.cn }}</td><td><ul><li ng-repeat=\"auth in user.authorities\"><a ng-click=openRemoveRoleFromUserModal(auth,user)><i class=icon-remove-sign style=color:red></i></a> {{ auth.authority }}</li><li><a ng-click=openAddRoleModal(user)><i class=icon-plus></i> Add Role(s)</a></li></ul></td><td><ul><li><a ng-click=openDisableUserModal(user)><i class=icon-ban-circle></i> Disable</a></li><li><a ng-click=deleteUserModal(user)><i class=icon-remove-sign style=color:red></i> Delete</a></li></ul></td></tr></tbody></table></div></div></div><div class=row-fluid ng-if=\"disabledUsers.length > 0\"><div class=widget><div class=widget-header><i class=icon-ban-circle style=color:red></i><h3>Disabled Users <span class=\"badge badge-important\">{{disabledUsers.length}}</span></h3><form class=form-search style=display:inline><input class=search-query ng-model=searchDisabled id=searchDisabled placeholder=Search></form></div><div class=widget-content><table class=\"widget-table table table-striped\"><thead><tr><th>UserId</th><th>Username</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"user in disabledUsers | filter:searchDisabled\"><td>{{ user.id }}</td><td>{{ user.cn }}</td><td><ul><li><a ng-click=enableUser(user)><i class=icon-ok></i> Enable</a></li><li><a ng-click=deleteUserModal(user)><i class=icon-remove-sign style=color:red></i> Delete</a></li></ul></td></tr></tbody></table></div></div></div></div><div class=span8></div>");
}]);
