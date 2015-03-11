angular.module('TicTacToe').controller('GamesController', ['$scope', '$resource', 'StompService', function($scope, $resource, StompService) {
    /** @type {Array<Game>} */
    $scope.games = $resource('/games/').query();

    /** @param {Game} game */
    StompService.subscribe($scope, '/games', function (game) {
        $scope.games.push(game);
    });

    $scope.createGame = function () {
        $resource('/games/').save();
    };
}]);