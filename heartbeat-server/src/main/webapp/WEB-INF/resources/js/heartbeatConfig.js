requirejs.config({
    "baseUrl": "resources/js/lib",
    "paths": {
        ko: "knockout",
        jquery: "jquery",
        jqueryui: "jquery-ui",
        "jquery.validate": "jquery.validate",
        "jquery.bootstrap": "bootstrap",
        sammy: "sammy",
        "app": "../app"
    },
    "shim": {
        ko: {exports: "ko"},
        jquery: {exports: 'jQuery'},
        "jquery.validate": {
            deps: ["jquery"]
        },
        "jquery.bootstrap": {
            deps: ["jquery"]
        },
        jqueryui: {
            deps: ['jquery']
        },
        sammy: {
          deps: ['jquery']  
        },
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);
