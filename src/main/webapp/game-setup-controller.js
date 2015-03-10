angular.module('TicTacToe').controller('GameSetupController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        $scope.joinGame = function () {
            StompService.send('/game/' + $routeParams.id + '/join');
        };

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-joined',
            /** @param {User} user */
            function (user) {
                $scope.game.playingUsers[user.id] = {name: user.name, status: "REGISTERED"};
            }
        );

        $scope.joined = function (user, game) {
            return game.playingUsers.hasOwnProperty(user.id);
        };

        $scope.ready = function () {
            StompService.send('/game/' + $routeParams.id + "/ready");
        };

        /**
         * @typedef {Object} PlayerStatusChanged
         * @property {String} playerId
         * @property {String} status
         */
        StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-status-changed',
            /** @param {PlayerStatusChanged} playerStatusChanged */
            function (playerStatusChanged) {
                $scope.game.playingUsers[playerStatusChanged.playerId].status = playerStatusChanged.status;
            }
        );
    }
]);