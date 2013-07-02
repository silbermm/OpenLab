define(["knockout", "jquery", "jquery.bootstrap"], function(ko, $) {
    
    var imageViewModel = function() {
        var self = this;
        self.images = ko.observableArray();
        
        self.findImages = function(){
          $.getJSON($("#base-url").val() + "/")  
        };
        
    
    };
    return imageViewModel;    
});


