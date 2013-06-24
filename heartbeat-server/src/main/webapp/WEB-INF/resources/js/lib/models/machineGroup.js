define(["knockout", "models/machine"],function(ko, Machine){
	
	var MachineGroup = function(data){
		var self = this;
		self.groupId = ko.observable(data.groupId);
		self.name = ko.observable(data.name);
		self.description = ko.observable(data.description);
		self.machines = ko.observableArray();

		$.each(data.machines, function(machine){
			var m = new Machine(machine);
			self.machines.push(m);	
		});

		self.expanded = ko.observable(false);
		self.currentClass = ko.computed(function() {
			if(self.expanded()){
				return "icon-caret-down";
			} else {	
				return "icon-caret-right";
			}
		});

	}

	return MachineGroup;

});
