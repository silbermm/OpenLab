'use strict';
angular.module('heartbeat.profile', [
    'ui.router.state',
    'authService'
]).config(function config($stateProvider) {
    $stateProvider.state('profile', {
        url: '/profile',
        views: {
            "main": {
                controller: "ProfileCtrl",
                templateUrl: 'profile/profile.tpl.html',
                data:{ pageTitle: 'Profile' }
            }
        }
    })
}).controller('ProfileCtrl', function ProfileController($scope, authService, $log) {
	authService.getRoles().then(function(data){
		$log.debug(data);
		if(data.status === 200){
			$scope.roles = data.data;
		} else {
			// should we redirect to the login page?
		}
	});
})

