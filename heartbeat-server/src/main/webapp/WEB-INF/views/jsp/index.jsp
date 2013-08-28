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
        <script type="text/javascript" src="<c:url value='/resources/js/vender/angular.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-bootstrap-0.5.0.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-route.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/vender/ui-utils.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/app.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/services/titleService.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/home/home.js' />"></script>
        <!-- <script type="text/javascript" src="<c:url value='/resources/js/controllers/.js'/>"></script> -->
        <!-- <script type="text/javascript" src="<c:url value='/resources/js/filters/.js' />"></script> -->
        <!-- <script type="text/javascript" src="<c:url value='/resources/js/directives/.js' />"></script> -->
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
