define(["knockout", "jquery", "jquery.bootstrap"], function(ko, $) {
    
    var imageViewModel = function() {
        var self = this;
        self.images = ko.observableArray();
        self.selectedImage = ko.observable();
        
        self.activeCss = ko.computed(function(){
           if(self.selectedImage()){
               return ""; 
           }
           return "disabled";
        });
        
        self.findImages = function(){
          $.getJSON($("#base-url").val() + "/show/images", function(data){
              var mappedImages = $.map(data, function(image) {
                  return image;
              });              
              self.images(mappedImages);
          }).fail(function(jqXHR){
             console.log("Unable to display images... THERE ARE NONE!") 
          });
        };  
                
    };
    return imageViewModel; 
});


