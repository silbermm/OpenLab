define(["knockout"], function(ko) {
    var Breadcrumb = function(name, path) {
        var self = this;
        self.name = ko.observable(name);
        self.path = ko.observable(path);
        self.isActive = ko.computed(function(){
            if(self.path()){
                return true;
            }
            return false;
        })
    }
    return Breadcrumb;
});