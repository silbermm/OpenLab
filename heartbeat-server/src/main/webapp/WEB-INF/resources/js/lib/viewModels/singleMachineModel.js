define(["knockout", "jquery", "models/machine", "models/machineGroup"], function(ko, $, Machine, MachineGroup) {
    var singleMachineModel = function(uuid) {
        var self = this;
        
        self.singleMachine = ko.observable();
        
        self.loadMachine = function(uuid){
            var baseUrl = $("#base-url").val();            
            $.getJSON(baseUrl + "/show/machine/uuid/" + uuid, function(data) {
                var currentMachine = new Machine(data);
                self.singleMachine(currentMachine);
            });
        };
        
        
    };
    return singleMachineModel;
});
