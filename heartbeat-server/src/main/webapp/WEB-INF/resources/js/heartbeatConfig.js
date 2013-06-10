requirejs.config({
    "baseUrl": "resources/js/lib",
    "paths": {
      "app": "../app"
    },
    "shim": {
			  "jquery": { exports: 'jQuery' },
        "jquery.validate": ["jquery"],
        "compViewModel" : ["knockout"],
				"jquery-ui" : {
					deps: ['jquery']
        },
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);
