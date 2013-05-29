define(["jquery", "knockout", "compViewModel"], function($, ko, compViewModel) {
    ko.applyBindings(new compViewModel());
});