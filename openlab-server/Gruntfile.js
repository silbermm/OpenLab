module.exports = function(grunt) {
	grunt
			.initConfig({
				pkg : grunt.file.readJSON('package.json'),	
				concat: {
			 		basic_and_extras: {
						files: {
							"src/main/webapp/WEB-INF/assets/js/common.js" : [
									'src/main/webapp/WEB-INF/assets/dev/jquery/jquery.js',
									'src/main/webapp/WEB-INF/assets/dev/angular/angular.js',
									'src/main/webapp/WEB-INF/assets/dev/angular-resource/angular-resource.js',
									'src/main/webapp/WEB-INF/assets/dev/angular-growl/build/angular-growl.js',
									'src/main/webapp/WEB-INF/assets/dev/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js',
									'src/main/webapp/WEB-INF/assets/dev/ng-grid/ng-grid-2.0.7.min.js',	
									'src/main/webapp/WEB-INF/assets/dev/angular-ui-router/release/angular-ui-router.js',
									'src/main/webapp/WEB-INF/assets/dev/js/app.js',
									'src/main/webapp/WEB-INF/assets/dev/js/**/*.js',
							],
			      },
			    },
			  },
				uglify : {
					options : {
						banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
						compress : {
							global_defs : {
								"DEBUG" : false
							},
							dead_code : true
						}
					},
					build : {
						files : {
							"src/main/webapp/WEB-INF/assets/js/common.min.js" : [
								"src/main/webapp/WEB-INF/assets/js/common.js" ],
						}
					}
				},
				less : {
					development : {
						options : {
							paths : [
									"src/main/webapp/WEB-INF/assets/dev/less/",
									"src/main/webapp/WEB-INF/assets/dev/bootstrap/less/",
									"src/main/webapp/WEB-INF/assets/dev/font-awesome/less/" ]
						},
						files : {
							"src/main/webapp/WEB-INF/assets/dev/css/main.css" : "src/main/webapp/WEB-INF/assets/dev/less/main.less"
						}
					},	
				},
				cssmin: {
					combine: {
						files: {
					    	'src/main/webapp/WEB-INF/assets/css/main.css': [
									'src/main/webapp/WEB-INF/assets/dev/css/main.css',
							 		'src/main/webapp/WEB-INF/assets/dev/angular-growl/build/angular-growl.min.css',
					    	 	'src/main/webapp/WEB-INF/assets/dev/ng-grid/ng-grid.css',	
									'src/main/webapp/WEB-INF/assets/dev/animate.css/animate.css'],
						}
					}
				},				
				watch : {
					scripts : {
						files : [ 
							'src/main/webapp/WEB-INF/assets/dev/js/*.js',							
							'src/main/webapp/WEB-INF/assets/dev/js/logs/*.js',	
							'src/main/webapp/WEB-INF/assets/dev/less/*.less' ],

						tasks : [ 'default' ],
						options : {
							spawn : false,
						},
					},
				}

			});

	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.registerTask('default', [ 'concat','uglify', 'less', 'cssmin' ]);
};
