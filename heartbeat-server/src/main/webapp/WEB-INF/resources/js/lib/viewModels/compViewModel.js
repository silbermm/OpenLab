define(["knockout", "models/machine", "models/machineGroup", "jquery"], function(ko, Machine, MachineGroup, $) {
    var compViewModel = function() {

        var self = this;

        self.groups = ko.observableArray();

        self.toggleGroup = function(machGroup) {
            machGroup.expanded(!machGroup.expanded());
        }

        self.refresh = function() {
            $.getJSON($('#show-groups').val(), function(data) {
                var mappedGroups = $.map(data, function(group) {
                    return new MachineGroup(group)
                });
                self.groups(mappedGroups);
            });
        }
    }

    return compViewModel;
});
