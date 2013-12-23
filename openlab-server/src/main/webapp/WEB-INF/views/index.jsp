<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html ng-app="heartbeat" ng-controller="AppCtrl">
<head>
<title>Generic Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="Matt Silbernagel" />
<link href="<c:url value='/assets/css/main.css'/>" rel="stylesheet" />
<script src="<c:url value='/assets/js/common.js' />"></script>
<script src="<c:url value='/assets/js/templates.js' />"></script>
</head>
<body>	
	<div class="navbar navbar-static-top">
		 <div class="navbar-inner">
			
    			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
      				<span class="sr-only">Toggle navigation</span>
      				<span class="fa fa-bar"></span>
      				<span class="fa fa-bar"></span>
      				<span class="fa fa-bar"></span>
    			</button>
    			<a class="brand" href="#"> OpenLab </a>  				
				<div class="collapse nav-collapse">
					<ul class="nav navbar-nav">							
						<li ui-route="/groups" ng-class="{active: $state.includes('groups')}">
							<a href="#/groups/all/table"> <i class="fa fa-home"></i> Home </a>
						</li>
						<li ui-route="/settings/all" ng-class="{active: $state.includes('settings')}" ng-if="isAuthenticated">
							<a href="#/settings/users"> <i class="fa fa-gears"></i> Settings </a>
						</li>																			
					</ul>
			
					<ul class="nav navbar-nav pull-right">
						<li ng-if="!isAuthenticated">
							<a href="<c:url value='/login' />"> <i class="icon-lock"></i> Login </a>
						</li>
						<li ng-if="isAuthenticated" ui-route="/profile" ng-class="{active: $state.includes('profile')}" >
							<a href="#/profile"> Logged in as: {{username}} </a>
						</li>						
						<li ng-if="isAuthenticated">
							<a href="<c:url value="/j_spring_security_logout" />"> <i class="fa fa-lock"></i> Logout </a>
						</li>			
					</ul>						
				</div>				
		</div>
	</div>
	
	<div growl class="growl-container"></div>
	<div ui-view="main" class="row-fluid"></div>
	</body>
</html>
