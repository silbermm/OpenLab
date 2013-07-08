define(["knockout", "jquery", "models/task"], function(ko, $, Task) {

    var taskViewModel = function() {
        var self = this;
        self.tasks = ko.observableArray();

        self.createTask = function(name, description, status) {
            $.ajax({
                url: $("#base-url").val() + "task/create",
                type: "POST",
                contentType: "application/json",
                data: "{name:" + name + ",description:" + description + ",status:" + status + ",percentComplete:0}",
            }).done(function(data) {
                var t = new Task(data);
                self.tasks.push(t);
            }).fail(function(jqXHR, textStatus) {

            });
        };

        self.removeTask = function(task) {

        };

        self.updateTask = function(task) {

        };

        self.refresh = function() {
            $.getJSON($('#base-url').val() + "task/show/all", function(data) {
                var mappedTasks = $.map(data, function(task) {
                    return new Task(task);
                });
                self.tasks(mappedTasks);
            });
        };

        (function poll() {
            setTimeout(function() {                
                var del = new Array();
                $.getJSON($('#base-url').val() + "task/show/all").done(function(data) {                    
                    if (self.tasks().length > 0) {                        
                        $.each(self.tasks(), function(tIdx, t) {
                            $.each(data, function(idx, d) {                                
                                if (t.id() === d.id) {
                                    t.status(d.status);
                                    t.completionPercentage(d.completionPercentage);
                                    del.unshift(idx);
                                }
                            });
                        });
                        $.each(del, function(i, val){
                            data.splice(val, 1);
                        });
                        $.each(data, function(i, val){
                           self.tasks.push(new Task(val)); 
                        });                        
                    } else {                       
                        var mappedTasks = $.map(data, function(task) {
                            return new Task(task);
                        });
                        self.tasks(mappedTasks);
                    }
                }).always(function() {                    
                    poll();
                });
            }, 5000);
        })();
    };
    return taskViewModel;
});

