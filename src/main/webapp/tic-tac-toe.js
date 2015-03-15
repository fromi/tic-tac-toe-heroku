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
    .filter('escape', function () {
        return window.encodeURIComponent;
    })
    .run(['$rootScope', '$resource', '$location', function ($rootScope, $resource, $location) {
        /**
         * @typedef {Object} User
         * @property {String} id
         * @property {String} name
         */
        $rootScope.user = $resource('/user').get();

        $rootScope.location = $location;
    }]);