'use strict';
angular.module('computerHttp', []).factory('computerHttp', function($http){        
    return {
        getAll : function(){            
            var compData = $http.get('machine/all').success(function(data){                
                return data;
            });
            return compData;
        },
        getById : function(id){
            var compData;
            $http.get('machine/' + id).success(function(data){
                console.log(data);
                compData = data;
            }).error(function(data, status, headers, config){
                console.log(data);
            });
            return compData;
        },
        save : function(){
            
        }
        
    }        
});