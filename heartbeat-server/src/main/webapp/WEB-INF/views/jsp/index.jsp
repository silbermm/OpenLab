<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript" src="<c:url value='/resources/js/vender/angular.min.js'/>"></script>
        <script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/bootstrap.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/angular-resource.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-bootstrap-0.5.0.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-route.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-utils.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/underscore.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/restangular.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/app.js' />"></script>        
        <script type="text/javascript" src="<c:url value='/resources/js/services/titleService.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/home/home.js' />"></script>        
        <script type="text/javascript" src="<c:url value='/resources/js/computer/computer.js' />"></script>  
        <script type="text/javascript" src="<c:url value='/resources/js/groups/groups.js' />"></script>  
    </head>
    <body>    
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">  
                
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>

                    <a class="brand" ui-route="/home">Heartbeat</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li ui-route="/home" ng-class="{active:$uiRoute}">
                                <a> <i class="icon-home"></i> Home </a>
                            </li>
                            <li ui-route="/settings" ng-class="{active:$uiRoute}" >
                                <a> <i class="icon-gears"></i> Settings </a>
                            </li>
                        </ul>
                        <form class="navbar-search pull-right" id="search-form">
                            <input type="text" class="search-query" placeholder="Search" />
                        </form>
                    </div>                
            </div>
        </div>       

        <nav id="group-sidebar" class="span3">
            <h4 class="dashboard">
                <a href=""#"> <i class="icon-home"></i> Dashboard </a>                 
            </h4> 
            <ul class="groups" >
                <li class="closed" ng-repeat="group in groups">
                    <span class="toggle"><i class="icon-caret-right"></i></span>
                    <h4 class="groupName"> <a href="#/groups/{{group.groupId}}" > {{ group.name }} </a> </h4> 
                    <span class='loader'>Loading</span>
                    <div class="options dropdown">
                        <a dropdown-toggle> <i class="icon-cog"></i> </a>
                        <ul class="dropdown-menu">
                            <li ng-repeat="choice in optionsItems">
                                <a>{{choice.display}}</a>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </nav>

        <div class="span9" id="ui-view-main" ui-view="main"> </div>
    </body>
</html>
