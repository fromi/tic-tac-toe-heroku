angular.module('TicTacToe', ['ngResource']).run(['$rootScope', '$resource', function ($rootScope, $resource) {
    var socket = new SockJS("/test");
    var client = Stomp.over(socket);
    client.connect({}, function() {
        client.subscribe("/games", function(data) {
            $rootScope.games.push({'id': data.body});
        });
    });

    $rootScope.user = $resource('/user').get();
    $rootScope.games = [
        {'id': 0}
    ];

    $rootScope.createGame = function () {
        $resource('/game').save();
    };


}]);