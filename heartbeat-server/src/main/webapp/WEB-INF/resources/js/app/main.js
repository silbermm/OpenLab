define(["knockout", "sammy", "viewModels/masterViewModel", "jquery"], function(ko, Sammy, masterViewModel, $) {

    var master = new masterViewModel();
    ko.applyBindings(master, document.getElementById("topHtml"));
    ko.bindingHandlers.stopBinding = {
        init: function() {
            return {controlsDescendantBindings: true};
        }
    };
    master.computerGroups.refresh();
  
    // Client-side routes    
    Sammy(function() {

        this.get('#Inventory', function() {
            master.pageTitle("Home");
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
        
        this.get('#group/:groupid', function(){
           var groupId = this.params['groupid'];
           master.routes.chosenNavId("Group");
           master.computerGroups.loadGroup(groupId);
           master.routes.removeAllBreadcrumbs();
           master.routes.addToBreadcrumbTrail("Inventory", false);
           master.routes.addToBreadcrumbTrail(master.computerGroups.currentGroup().name(), "#group/" + master.computerGroups.currentGroup().groupId());
        });

        this.get("", function() {
            master.pageTitle("Home");
            master.routes.chosenNavId("Inventory");
            master.routes.removeAllBreadcrumbs();
            master.routes.addToBreadcrumbTrail("Inventory", null);
        });

    }).run();
});
