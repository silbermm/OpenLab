define(["knockout", "models/machine", "models/machineGroup", "jquery", "jquery.bootstrap", "jqueryui"], function(ko, Machine, MachineGroup, $) {
    var compViewModel = function() {

        var self = this;
        self.groups = ko.observableArray();
        self.totalMachines = ko.observable();
        self.chosenGroup = ko.observable();

        self.toggleGroup = function(machGroup) {
            machGroup.expanded(!machGroup.expanded());
        };

        self.loadGroup = function(groupId) {
            // Should we try to get fresh data?
            $.each(self.groups(), function(idx, group) {
                if (group.id() === groupId) {
                    self.chosenGroup(group);
                }
            });
        };

        self.refresh = function() {
            var total = 0;
            $.getJSON($('#show-groups').val(), function(data) {
                var mappedGroups = $.map(data, function(group) {
                    total += group.machines.length;
                    return new MachineGroup(group);
                });
                self.groups(mappedGroups);
                self.totalMachines(total);
                $.each(self.groups(), function(idx, g) {
                    var $gid = "#" + g.linkId();
                    $($gid).droppable({
                        accept: "#machine-list > li",
                        activeClass: "",
                        over: function(event, ui) {
                            console.log("the over event fired");
                            g.hover(true);
                        },
                        out: function(event, ui) {
                            console.log("the out event fired");
                            g.hover(false);
                        },
                        drop: function(event, ui) {
                            g.hover(false);
                            self.moveComp(ui.draggable.attr("id"), g.groupId());
                        },
                    });
                    $.each(g.machines(), function(i, m) {
                        self.makeCompDraggable( "sidebar-comp-" + m.uid() );
                    });
                });
            });
        };

        self.makeCompDraggable = function(id) {
            $("#" + id).draggable({
                revert: "invalid",
                containment: "document",
                helper: "clone",
                cursor: "move",
            });
        }

        self.moveComp = function(mid, toGroupId) {
            var $machine;
            var $newGroup;
            var $oldGroup;
            $.each(self.groups(), function(idx, oldGroup) {               
                if (oldGroup.groupId() === toGroupId) {                    
                    $newGroup = oldGroup;
                }
                ;
                $.each(oldGroup.machines(), function(i, m) {
                    if (m && "sidebar-comp-" + m.uid() === mid) {
                        $machine = m;
                        $oldGroup = oldGroup;
                    }
                    ;
                });
            });
            $.ajax({
                url: $("#base-url").val() + "/move/machine/" + $machine.uid() + "/to/" + $newGroup.groupId(),
                type: "PUT",                
            }).done(function(data){
                $oldGroup.machines.remove($machine);
                $newGroup.machines.push($machine);
                $newGroup.expanded(true);
                $("#sidebar-comp-" + $machine.uid()).effect("highlight", {}, 500);
                self.makeCompDraggable( "sidebar-comp-" + $machine.uid());                                
            }).error(function(jqXHR){
                console.log(jqXHR.responseText.message);
            });
            
        };

    };
    return compViewModel;
});
