angular.module('TicTacToe').controller('GameController', ['$scope', '$resource', '$routeParams', 'StompService', function($scope, $resource, $routeParams, StompService) {

    /**
     * @typedef {Object} RegisteredPlayer
     * @property {User} user
     * @property {boolean} ready
     */
    /**
     * @typedef {Object} Game
     * @property {string} id
     * @property {Array<RegisteredPlayer>} registeredPlayers
     */
    $scope.game = $resource('/game/' + $routeParams.id).get();

    $scope.joinGame = function () {
        StompService.send('/game/' + $routeParams.id + '/join');
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/joined', function (user) {
        $scope.game.registeredPlayers.push({user:user, ready:false});
    });

    $scope.joined = function(user, game) {
        for (var i = 0; i < game.registeredPlayers.length; i++) {
            if (game.registeredPlayers[i].user.id == user.id) {
                return true;
            }
        }
        return false;
    };

    $scope.ready = function () {
        StompService.send('/game/' + $routeParams.id + "/ready");
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/ready', function (user) {
        for (var i = 0; i < $scope.game.registeredPlayers.length; i++) {
            if ($scope.game.registeredPlayers[i].user.id == user.id) {
                $scope.game.registeredPlayers[i].ready = true;
            }
        }
    });
}]);