define(["knockout", "sammy", "viewModels/masterViewModel"], function(ko, Sammy, masterViewModel) {

    var master = new masterViewModel();
    ko.applyBindings(master);
    ko.bindingHandlers.stopBinding = {
        init: function() {
            return {controlsDescendantBindings: true};
        }
    };
    

    // Client-side routes    
    Sammy(function() {

        this.get('#Inventory', function() {
            master.routes.chosenNavId("Inventory");
            master.routes.removeAllBreadcrumbs();
            master.routes.addToBreadcrumbTrail("Inventory", "#Inventory");
            master.computerGroups.refresh();
        });

        this.get('#Settings', function() {
           master.routes.chosenNavId("Settings");
           master.routes.removeAllBreadcrumbs();
           master.routes.addToBreadcrumbTrail("Settings", "#Settings");           
        });
        
        this.get('#machine/:uuid', function(){
           console.log("in the machine/uuid url");
           var uuid = this.params['uuid'];
           master.routes.chosenNavId("Machine");
           master.computer.loadMachine(uuid);           
           master.routes.removeAllBreadcrumbs();           
           //master.routes.addToBreadcrumbTrail("Inventory", "#Inventory");
           //master.routes.addToBreadcrumbTrail(master.computer.singleComputer.groupName(), "#group/" + master.computer.singleComputer.group());
           //master.routes.addToBreadcrumbTrail(master.computer.singleComputer.name(), "#machine/" + master.computer.singleComputer.uid());           
        });

        this.get("", function() {
            master.routes.chosenNavId("Inventory");
            master.routes.removeAllBreadcrumbs();
            master.routes.addToBreadcrumbTrail("Inventory", "#Inventory");
            master.computerGroups.refresh();
        });

    }).run();
});
