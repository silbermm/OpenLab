define(["knockout", "models/machine"], function(ko, Machine) {

    var MachineGroup = function(data) {
        var self = this;
        self.groupId = ko.observable(data.groupId);
        self.name = ko.observable(data.name);
        self.description = ko.observable(data.description);
        self.machines = ko.observableArray();

        var mappedMachines = $.map(data.machines, function(m) {
            return new Machine(m);
        });
        
        self.machines(mappedMachines);
        self.expanded = ko.observable(false);
        
        self.hover = ko.observable(false);
        
        self.currentClass = ko.computed(function() {
            if (self.expanded()) {
                return "icon-caret-down";
            } else {
                return "icon-caret-right";
            }
        });
        self.hiddenClass = ko.computed(function() {
            if (!self.expanded()) {                
                return "hidden";
            }
        });
        self.linkId = ko.computed(function() {
            return "groupId-" + self.groupId();
        });
        
        self.folderIcon = ko.computed(function() {
            if(self.hover()){
                return "icon-folder-open";
            }
            return "icon-folder-close";
        });
    };

    return MachineGroup;
});
