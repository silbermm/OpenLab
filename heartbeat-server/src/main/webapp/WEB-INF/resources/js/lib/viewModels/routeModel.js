define(["knockout", "jquery", "models/navigation", "models/breadcrumb"], function(ko, $, Navigation, Breadcrumb) {
    var routeModel = function() {
        var self = this;
        self.navigation = [
            new Navigation("Inventory", "icon-sitemap"),
            new Navigation("Settings", "icon-cogs")
        ];  
        
        self.addToBreadcrumbTrail = function(name, path){
            self.currentBreadcrumbs.push(new Breadcrumb(name, path));
        };
        
        self.removeFromBreadcrumbTrail = function(name){
            $.each(self.currentBreadcrumbs(), function(item){
                if(item.name === name){
                    self.currentBreadcrumbs.remove(item);                    
                }
            });
            self.currentBreadcrumbs.remove(name);
        };
        
        self.removeAllBreadcrumbs = function(){
          self.currentBreadcrumbs.removeAll();  
        };
        
        self.chosenNavId = ko.observable("Inventory");                           
        
        self.currentActiveNav = ko.observable("Inventory");
        
        self.isInventory = ko.computed(function() {
            return (self.chosenNavId() === "Inventory");
        }, this);
        
        self.isMachine = ko.computed(function() {
           return (self.chosenNavId() === "Machine"); 
        });
        
        self.isGroup = ko.computed(function() {
           return (self.chosenNavId() === "Group"); 
        });
        
        self.isSettings = ko.computed(function() {
            return (self.chosenNavId() === "Settings");
        }, this);
        
        self.currentBreadcrumbs = ko.observableArray();        
        
        self.goToNav = function(nav) {
            location.hash = nav.name;
        };
    };
    return routeModel;
});
