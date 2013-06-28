define(["knockout"], function(ko) {
    var Machine = function(data) {
        var self = this;              
        self.id = ko.observable(data.id);
        self.uid = ko.observable(data.uid);
        self.name = ko.observable(data.name);
        self.serialNumber = ko.observable(data.serialNumber);
        self.mac = ko.observable(data.mac);
        self.ipAddress = ko.observable(data.ipAddress);
        self.os = ko.observable(data.os);
        self.osVersion = ko.observable(data.osVersion);
        self.manufacturer = ko.observable(data.manufacturer);
        self.model = ko.observable(data.model);
        self.lastSeen = ko.observable(data.lastSeen);
        if (data.group) {
            self.group = ko.observable(data.group.groupId);
            self.groupName = ko.observable(data.group.name);
        }
        self.currentUser = ko.observable(data.currentUser);
        self.sidebarId = ko.computed(function(){
            return "sidebar-comp-" + self.uid();
        });
        self.isLoggedIn = ko.computed(function(){
           if(self.currentUser() == "none" || self.currentUser() == 0) {
                return "available";
           } else if(self.currentUser() == "unavailable") {
                return "offline";
           }
           return "logged-in";
        });                
        self.infoIconId = ko.computed(function(){
           return "info-icon-" + self.uid();
        });
        self.machineDropDownId = ko.computed(function(){
           return "btn-dropdown-" + self.uid(); 
        });
        self.machineLinkId = ko.computed(function(){
           return "machine-link-" + self.uid(); 
        });
        self.showComputer = function(){
            //var path = $("#base-url").val();
            location.hash = "machine/" + self.uid();         
        };
    }
    return Machine;
});
