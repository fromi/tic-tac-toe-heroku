angular.module('TicTacToe', ['ngResource', 'ngRoute'])
    .config(function($resourceProvider) {
        $resourceProvider.defaults.stripTrailingSlashes = false;
    })
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'games.html',
            controller: 'GamesController'
        }).when('/game/:id', {
            templateUrl: 'game/game.html',
            controller: 'GameController'
        }).otherwise({
            redirectTo: '/'
        });
    }])
    .run(['$rootScope', '$resource', function ($rootScope, $resource) {
        /**
         * @typedef {Object} User
         * @property {string} id
         * @property {string} name
         */
        $rootScope.user = $resource('/user').get();
    }]);