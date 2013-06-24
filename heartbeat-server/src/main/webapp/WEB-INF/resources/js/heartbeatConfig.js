requirejs.config({
    "baseUrl": "resources/js/lib",
    "paths": {
			ko: "knockout",
      jquery: "jquery",
			jqueryui: "jquery-ui",
			jqueryVal: "jquery.validate",
			//compViewModel: "viewModels/compViewModel",
      "app": "../app"
    },
    "shim": {
			  ko: {exports: "ko"},
			  jquery: { exports: 'jQuery' },
        jqueryVal: {
					deps: ["jquery"]
				},
				jqueryui : {
					deps: ['jquery']
        },
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);
