'use strict';
angular.module('heartbeat.profile', [
    'ui.state',
    'titleService',
    'authService'
]).config(function config($stateProvider) {
    $stateProvider.state('profile', {
        url: '/profile',
        views: {
            "main": {
                controller: "ProfileCtrl",
                templateUrl: 'resources/js/profile/profile.tpl.html'
            }
        }
    })
}).controller('ProfileCtrl', function ProfileController($scope, titleService, authService, $log) {
	titleService.setTitle("User Profile");
	authService.getRoles().then(function(data){
		if(data.status === 200){
			$scope.roles = data.data;
		} else {
			// should we redirect to the login page?
		}
	});
})

