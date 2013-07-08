define(["knockout"], function(ko) {
  var Task = function(id, name, description, status, currentPercentage) {
      var self = this;
      self.id = ko.observable(id);
      self.name = ko.observable(name);
      self.description = ko.observable(description);
      self.status = ko.observable(status);
      self.currentPercentage = ko.observable(currentPercentage);
  };      
  return Task;    
});