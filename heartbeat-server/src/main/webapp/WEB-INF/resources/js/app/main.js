define(["knockout", "viewModels/compViewModel"], function(ko, compViewModel) {
 	var compModel = new compViewModel();	
	ko.applyBindings(compModel);
        compModel.refresh();
});
