angular.module('TicTacToe').service('StompService', ['$q', function($q) {
    var socket = new SockJS("/web-socket");
    var client = Stomp.over(socket);
    var connection = $q.defer();
    client.connect({}, function() {
        connection.resolve();
    });

    return {
        subscribe: function(scope, destination, callback, headers) {
            connection.promise.then(function() {
                var subscription = client.subscribe(destination, function(message) {
                    scope.$apply(callback(JSON.parse(message.body)));
                }, headers);
                scope.$on('$destroy', function(){
                    subscription.unsubscribe();
                });
            });
        },

        send: function(destination, headers, body) {
            connection.promise.then(function() {
                client.send(destination, headers, body);
            });
        }
    };
}]);