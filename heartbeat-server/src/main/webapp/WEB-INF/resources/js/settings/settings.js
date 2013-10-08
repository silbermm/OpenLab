'use strict';
angular.module('heartbeat.settings', [
    'ui.state',
    'titleService',
    'ngGrid',
    'ui.bootstrap'
]).config(function config($stateProvider, $urlRouterProvider) {
    $stateProvider.state('settings', {
        abstract: true,
        url: '/settings',
        views: {
            "main": {
                controller: "SettingsCtrl",
                templateUrl: 'resources/js/settings/settings.tpl.html'
            }
        }
    }).state('settings.all', {
        url: '/all',
        templateUrl: 'resources/js/settings/all/settings.all.tpl.html',
    })
}).controller('SettingsCtrl', function SettingsController($scope, titleService, $dialog, $stateParams, $http) {
  
  


})
