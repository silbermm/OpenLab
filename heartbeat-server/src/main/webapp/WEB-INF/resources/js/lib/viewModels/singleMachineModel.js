define(["knockout", "jquery", "models/machine", "models/machineGroup", "models/partition", "jquery.bootstrap"], function(ko, $, Machine, MachineGroup, Partition) {
    var singleMachineModel = function(uuid) {
        var self = this;

        self.computer = ko.observable();
        self.rebootOs = ko.observable();
        self.osOptions = ko.observableArray();

        self.loadMachine = function(uuid) {
            console.log("loading machine " + uuid);
            var baseUrl = $("#base-url").val();
            $.ajax({
                async: false,
                url: baseUrl + "/show/machine/uuid/" + uuid,
                dataType: "json",
                type: "GET",
            }).done(function(data) {
                console.log("done loading machine data...");
                self.computer(new Machine(data));
            });
        };

        self.activeCss = ko.computed(function() {
            if (self.rebootOs()) {
                return "";
            }
            return "disabled";
        });

        self.getOsOptions = function(machine) {
            self.osOptions.removeAll();
            if (machine.partition1()) {
                console.log("adding " + machine.partition1())
                self.osOptions.push(new Partition(1, machine.partition1()));
            }
            if (machine.partition2()) {
                self.osOptions.push(new Partition(2, machine.partition2()));
            }
            if (machine.partition3()) {
                self.osOptions.push(new Partition(3, machine.partition3()));
            }
            if (machine.partition4()) {
                self.osOptions.push(new Partition(4, machine.partition4()));
            }
        }

        self.sidebarId = function(machine) {
            return "sidebar-comp-" + machine.uid();
        };

        self.prepareReboot = function(currentComp) {
            self.getOsOptions(currentComp);
            if (currentComp.partition1 && (currentComp.partition2 || currentComp.partition3 || currentComp.partition4)) {
                $("#rebootChoiceModal").modal('show');
            } else {
                $("#rebootNoChoiceModal").modal('show');
            }
        };

        self.closeRebootWindow = function() {
            $("#rebootChoiceModal").modal('hide');
            $("#rebootNoChoiceModal").modal('hide');
        };

        self.reboot = function(machine) {
            console.log("**************** " + self.rebootOs() + " ***************");
            self.closeRebootWindow();
            $.ajax({
                url: $("#base-url").val() + "tasks/reboot/" + self.computer().id() + "/to/" + self.rebootOs(),
                type: "PUT",                
                contentType: "application/json",
                dataType: "json",
            }).done(function(data) {
                alert("rebooting...");
            }).fail(function(jqXHR, textStatus) {
                alert(jqXHR.responseText);
            }).always(function() {
                self.rebootOs(null);
            });
        }

        self.preparePullClone = function(currentComp) {
            $('#compModal').modal('show');
        };

        self.closeCloneWindow = function() {
            $('#compModal').modal('hide');
        };
    };
    return singleMachineModel;
});
