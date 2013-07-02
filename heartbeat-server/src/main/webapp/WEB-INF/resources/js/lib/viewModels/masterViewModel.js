define(["knockout", "jquery", "viewModels/compViewModel", "viewModels/routeModel", "viewModels/singleMachineModel", "viewModels/imageViewModel", "models/machine"], function(ko, $, compViewModel, routeModel, singleMachineModel, Machine) {
    var masterViewModel = function() {
        var self = this;
        self.computerGroups = new compViewModel();
        self.routes = new routeModel();
        self.imageModel = new imageViewModel();
        self.comp = new singleMachineModel();
        self.pageTitle = ko.observable("Page Title");        
        
        /*
        self.loadMachine = function(uuid){
            var baseUrl = $("#base-url").val();   
            $.ajax({
                async: false,
                url: baseUrl + "/show/machine/uuid/" + uuid,
                dataType: "json",
                type: "GET",                
            }).done(function(data){
                self.computer(new Machine(data));                
            });
        };
        */
       /*
        self.preparePullClone = function(currentComp){
          $('#compModal').modal('show');          
        };
        */
        
    };
    return masterViewModel;
});
