angular.module('searchService', []).factory('searchService', function(){
    var placeholder = 'Search All Items';     
    return {
       setPlaceholder : function(s){
          placeholder = s;
          $("#search").attr("placeholder", s);
       },
       getPlaceholder : function(){
        return placeholder;
       },       
    }
    
});