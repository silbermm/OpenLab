define(["knockout", "sammy", "viewModels/masterViewModel", "jquery"], function(ko, Sammy, masterViewModel, $) {

    var master = new masterViewModel();
    ko.applyBindings(master, document.getElementById("topHtml"));
    ko.bindingHandlers.stopBinding = {
        init: function() {
            return {controlsDescendantBindings: true};
        }
    };
    master.computerGroups.refresh();
    master.pageTitle("Home");
    
    
    
    // Client-side routes    
    Sammy(function() {

        this.get('#Inventory', function() {
            master.routes.chosenNavId("Inventory");
            master.routes.removeAllBreadcrumbs();
            master.routes.addToBreadcrumbTrail("Inventory", false);                     
        });

        this.get('#Settings', function() {
           master.pageTitle("Settings");
           master.routes.chosenNavId("Settings");
           master.routes.removeAllBreadcrumbs();
           master.routes.addToBreadcrumbTrail("Settings", false);           
        });
        
        this.get('#machine/:uuid', function(){           
           var uuid = this.params['uuid'];
           master.routes.chosenNavId("Machine");
           master.comp.loadMachine(uuid); 
           master.imageModel.findImages();
           master.pageTitle(master.comp.computer().name());
           master.routes.removeAllBreadcrumbs();           
           master.routes.addToBreadcrumbTrail("Inventory", "#Inventory");
           master.routes.addToBreadcrumbTrail(master.comp.computer().groupName(), "#group/" + master.comp.computer().group());
           master.routes.addToBreadcrumbTrail(master.comp.computer().name(), false);           
        });

        this.get("", function() {
            master.routes.chosenNavId("Inventory");
            master.routes.removeAllBreadcrumbs();
            master.routes.addToBreadcrumbTrail("Inventory", null);
        });

    }).run();
});
