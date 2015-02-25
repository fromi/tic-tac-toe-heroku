angular.module('TicTacToe', ['ngResource', 'ngRoute']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'games.html'
    }).when('/game/:id', {
        templateUrl: 'game.html'
    }).otherwise({
        redirectTo: '/'
    });
}]).run(['$rootScope', '$resource', '$routeParams', function ($rootScope, $resource, $routeParams) {
    var socket = new SockJS("/web-socket");
    var client = Stomp.over(socket);
    client.connect({}, function () {
        client.subscribe("/games", function (data) {
            var games = JSON.parse(data.body);
            $rootScope.games.push(games);
        });
    });

    $rootScope.user = $resource('/user').get();
    $rootScope.games = $resource('/games').query();

    $rootScope.createGame = function () {
        $resource('/game').save();
    };

    $rootScope.joinGame = function () {
        client.send("/app/join", {}, $routeParams.id);
    }
}]);