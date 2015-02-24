angular.module('TicTacToe', ['ngResource', 'ngRoute']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'games.html'
    }).when('/games/:gameId', {
        templateUrl: 'game.html'
    }).otherwise({
        redirectTo: '/'
    });
}]).run(['$rootScope', '$resource', function ($rootScope, $resource) {
    var socket = new SockJS("/web-socket");
    var client = Stomp.over(socket);
    client.connect({}, function () {
        client.subscribe("/games", function (data) {
            $rootScope.games.push(JSON.parse(data.body));
        });
    });

    $rootScope.user = $resource('/user').get();
    $rootScope.games = $resource('/games').query();

    $rootScope.createGame = function () {
        $resource('/game').save();
    };
}]);