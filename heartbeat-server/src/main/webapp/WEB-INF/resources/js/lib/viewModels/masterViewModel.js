define(["knockout", "jquery",
    "viewModels/compViewModel",
    "viewModels/routeModel",
    "viewModels/singleMachineModel",
    "viewModels/imageViewModel",
    "viewModels/taskViewModel",
    "models/task",
    "models/remoteImageTask"], function(ko, $, compViewModel,
        routeModel, singleMachineModel,
        imageViewModel, taskViewModel, Task, remoteImageTask) {
    var masterViewModel = function() {
        var self = this;
        self.computerGroups = new compViewModel();
        self.routes = new routeModel();
        self.imageModel = new imageViewModel();
        self.comp = new singleMachineModel();
        self.taskModel = new taskViewModel();
        self.pageTitle = ko.observable("Page Title");



        self.startClone = function() {
            //var t = new Task(null, "Clone Task", "Cloning " + self.comp.computer().name(), "Starting", 0);            
            //self.taskModel.createTask(t);

            var imageTask = new remoteImageTask(self.comp.computer().uid(),
                    self.comp.computer().serialNumber(),
                    self.comp.computer().mac(),
                    self.comp.computer().ipAddress(),
                    self.imageModel.selectedImage());
            // set the selected image back to nothing
            self.comp.closeCloneWindow();
            self.imageModel.selectedImage(null);
            $.ajax({
                url: $("#base-url").val() + "tasks/clone",
                type: "POST",
                data: ko.toJSON(imageTask),
                contentType: "application/json",
                dataType: "json",
            }).done(function(data) {
                t.status("In Progress");
            }).fail(function(jqXHR, textStatus, exObj) {
                var msg = jQuery.parseJSON(jqXHR.responseText);
                t.status("Failed: " + msg.message);
                t.currentPercentage(100);
            }).always(function(data) {

            });
        };

     
    };
    return masterViewModel;
});
