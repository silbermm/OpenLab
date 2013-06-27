define(["knockout", "jquery"], function(ko, $) {
    var routeModel = function() {
        var self = this;
        self.navigation = ["home","machines"];
        self.chosenNavId = ko.observable();        
        self.isDashboard = ko.computed(function() {
            return (self.chosenNavId() == "Dashboard");
        }, this);

        self.isCloning = ko.computed(function() {
            return (self.chosenNavId() == "Cloning");
        }, this);

        self.isSettings = ko.computed(function() {
            return (self.chosenNavId() == "Settings");
        }, this);

        self.goToNav = function(nav) {
            location.hash = nav.name;
        };
    };
    return routeModel;
});
