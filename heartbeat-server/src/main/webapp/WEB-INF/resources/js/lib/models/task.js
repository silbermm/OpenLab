define(["knockout"], function(ko) {
  var Task = function(data) {
      var self = this;
      self.id = ko.observable(data.id);
      self.name = ko.observable(data.name);
      self.description = ko.observable(data.description);
      self.status = ko.observable(data.status);
      self.completionPercentage = ko.observable(data.completionPercentage);
  };      
  return Task;    
});