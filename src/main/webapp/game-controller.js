angular.module('TicTacToe').controller('GameController', ['$scope', '$resource', '$routeParams', 'StompService', function($scope, $resource, $routeParams, StompService) {

    /**
     * @typedef {Object} GameSetup
     * @property {Array<RegisteredPlayer>} players
     */
    /**
     * @typedef {Object} RegisteredPlayer
     * @property {User} user
     * @property {boolean} ready
     */
    /**
     * @typedef {Object} Game
     * @property {string} id
     * @property {GameSetup} state
     */
    $scope.game = $resource('/game/' + $routeParams.id).get();

    $scope.joinGame = function () {
        StompService.send('/game/' + $routeParams.id + '/join');
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/joined', function (user) {
        $scope.game.state.players.push({user:user, ready:false});
    });

    $scope.joined = function(user, game) {
        for (var i = 0; i < game.state.players.length; i++) {
            if (game.state.players[i].user.id == user.id) {
                return true;
            }
        }
        return false;
    };

    $scope.ready = function () {
        StompService.send('/game/' + $routeParams.id + "/ready");
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/ready', function (user) {
        for (var i = 0; i < $scope.game.state.players.length; i++) {
            if ($scope.game.state.players[i].user.id == user.id) {
                $scope.game.state.players[i].ready = true;
            }
        }
    });
}]);