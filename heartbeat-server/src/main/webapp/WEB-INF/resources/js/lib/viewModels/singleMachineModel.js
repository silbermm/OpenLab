define(["knockout", "jquery", "models/machine", "models/machineGroup"], function(ko, $, Machine, MachineGroup) {
    var singleMachineModel = function(uuid) {
        var self = this;
        
        self.singleMachine = ko.observable();
        
        self.loadMachine = function(){
            $.getJSON($('#show-groups').val(), function(data) {
                
            });
        };
        
        
    };
    return singleMachineModel;
});
