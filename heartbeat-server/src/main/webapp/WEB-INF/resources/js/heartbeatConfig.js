requirejs.config({
    "baseUrl": "resources/js/lib",
    "paths": {
      "app": "../app"
    },
    "shim": {
        "jquery.validate": ["jquery"],
        "compViewModel" : ["knockout"],
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);