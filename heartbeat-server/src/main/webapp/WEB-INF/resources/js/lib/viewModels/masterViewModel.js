define(["jquery", "viewModels/compViewModel", "viewModels/routeModel", "viewModels/singleMachineModel"], function($, compViewModel, routeModel, singleMachineModel) {
    var masterViewModel = function() {
        var self = this;
        self.computerGroups = new compViewModel();
        self.routes = new routeModel();
        self.computer = new singleMachineModel();
    };
    return masterViewModel;
});
