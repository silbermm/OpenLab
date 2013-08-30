'use strict';
angular.module('computerService', ['ngResource']).factory('computerService', function($resource){        
    return $resource('machine/:id'),
        {id:'@id'},
        {
            query: {method: 'GET', params:{id: 'all'}, isArray:true},
            move: {method: 'PUT', params:{move:true}, url: 'machine/:id/to/:groupId'},            
        }    
});