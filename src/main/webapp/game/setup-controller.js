angular.module('TicTacToe').controller('GameSetupController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        $scope.joinGame = function () {
            StompService.send('/game/' + $routeParams.id + '/join');
        };

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-joined',
            /** @param {Player} player */
            function (player) {
                $scope.game.players[player.user.id] = player;
            }
        );

        $scope.joined = function (user, game) {
            return game.players.hasOwnProperty(user.id);
        };

        $scope.ready = function () {
            StompService.send('/game/' + $routeParams.id + "/ready");
        };

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-changed',
            /** @param {Player} player*/
            function (player) {
                $scope.game.players[player.user.id] = player;
            }
        );
    }
]);