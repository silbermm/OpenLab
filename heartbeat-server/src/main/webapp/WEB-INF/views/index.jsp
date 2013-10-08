<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="heartbeat" ng-controller="AppCtrl">
    <head>
        <title>Generic Page </title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="description" content="" />
        <meta name="author" content="Matt Silbernagel" />
        <link href="https://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.no-icons.min.css" rel="stylesheet" />
        <link href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" />
        <link href="<c:url value='/resources/css/heartbeat.css'/>" rel="stylesheet" />
        <script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/angular.min.js'/>"></script>        
        <script type="text/javascript" src="<c:url value='/resources/js/vender/bootstrap.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/angular-resource.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-bootstrap-tpls-0.6.0.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ng-grid.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-route.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-utils.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/underscore.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/app.js' />"></script>        
        <script type="text/javascript" src="<c:url value='/resources/js/services/titleService.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/services/searchService.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/home/home.js' />"></script>        
        <script type="text/javascript" src="<c:url value='/resources/js/computer/computer.js' />"></script>  
        <script type="text/javascript" src="<c:url value='/resources/js/groups/groups.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/settings/settings.js' />"></script>
    </head>
    <body>    
        <div class="navbar">
            <div class="navbar-inner">                  
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>

                    <a class="brand" ui-route="/home">Heartbeat</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li ui-route="/home" ng-class="{active: $state.includes('home')}">
                                <a href="#/home"> <i class="icon-home"></i> Home </a>
                            </li>
                            <security:authorize access="hasRole('ROLE_ADMINISTRATOR')"> 
                            <li ui-route="/settings" ng-class="{active: $state.includes('settings')}" >
                                <a href="#/settings"> <i class="icon-gears"></i> Settings </a>
                            </li>                            
                            </security:authorize>
                        </ul>
                        <ul class="nav pull-right">
                            <security:authorize access="isAnonymous()"> 
                                <li>
                                    <a href="<c:url value='/login' />"> <i class="icon-lock"></i> Login </a>
                                </li>
                             </security:authorize> 
                             <security:authorize access="isAuthenticated()"> 
                                 <li> 
                                     <a> Logged in as: <security:authentication property="principal.username" /> </a>                                  
                                 </li>
                                 <li>
                                     <a href="<c:url value="/j_spring_security_logout" />"> <i class="icon-lock"></i> Logout </a>
                                 </li>
                             </security:authorize>
                        </ul>
                        
                    </div>                
            </div>
        </div>      
        <div ui-view="main"> </div>
    </body>
</html>
