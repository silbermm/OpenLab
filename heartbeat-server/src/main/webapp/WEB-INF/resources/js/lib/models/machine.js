define(["knockout"], function(ko) {

	var Machine = function(data) {
		var self = this;
	 	self.id = ko.observable(data.id);
		self.uid = ko.observable(data.uid);
		self.name = ko.observable(data.name);
		self.serialNumber = ko.observable(data.serialNumber);
	  self.mac = ko.observable(data.mac);
		self.ipAddress = ko.observable(data.ipAddress);
		self.os = ko.observable(data.os);
		self.osVersion = ko.observable(data.osVersion);
		self.manufacturer = ko.observable(data.manufacturer);
		self.model = ko.observable(data.model);
		self.lastSeen = ko.observable(data.lastSeen);
		if(data.group){
			self.group = ko.observable(data.group.groupId);
		}
	  self.currentUser = ko.observable(data.currentUser);

		self.hiddenClass = ko.computed(function(){
			
		});

	}	
	
	return Machine;

});
