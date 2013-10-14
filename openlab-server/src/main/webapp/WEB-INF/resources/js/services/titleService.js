'use strict';

angular.module('titleService', []).factory('titleService', function($document){
    var suffix, title;
    suffix = title = "";
    return {
       setTitle : function(s){
          if(suffix !== ""){
              title = s + suffix;
          } else {
              title = s;
          }
          return $document.prop('title', title);
       },
       getTitle : function(){
        return $document.prop('title');
       },
       setSuffix : function(s){
        return suffix = s;
       },
       getSuffix : function() {
        return suffix;
       }
    }
    
})

