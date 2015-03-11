angular.module('TicTacToe').controller('GameController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        var gameResource = $resource('/game/' + $routeParams.id);

        /**
         * @typedef {Object} PlayingUser
         * @property {String} name
         * @property {String} status
         */
        /**
         * @typedef {Object} Game
         * @property {String} id
         * @property {String} state
         * @property {Object<string, PlayingUser>} playingUsers
         */
        $scope.game = gameResource.get();

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/game-started', function () {
            $scope.game = gameResource.get();
        });
    }
]);