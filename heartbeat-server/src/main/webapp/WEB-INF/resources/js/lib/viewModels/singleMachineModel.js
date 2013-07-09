define(["knockout", "jquery", "models/machine", "models/machineGroup","jquery.bootstrap"], function(ko, $, Machine, MachineGroup) {
    var singleMachineModel = function(uuid) {
        var self = this;

        self.computer = ko.observable();

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
               
       self.preparePullClone = function(currentComp){
          $('#compModal').modal('show');          
        };
        
        self.restart = function (currentComp){
            
        }
        
        self.closeCloneWindow = function(){
            $('#compModal').modal('hide');
        }
        
        

    };
    return singleMachineModel;
});
