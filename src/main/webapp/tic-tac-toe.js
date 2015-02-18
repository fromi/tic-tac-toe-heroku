angular.module('TicTacToe', ['ngResource', 'ngRoute']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'games.html'
    }).when('/games/:gameId', {
        templateUrl: 'game.html'
    }).otherwise({
        redirectTo: '/'
    });
}]).run(['$rootScope', '$resource', function ($rootScope, $resource) {
    var socket = new SockJS("/ws");
    var client = Stomp.over(socket);
    client.connect({}, function () {
        client.subscribe("/games", function (data) {
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