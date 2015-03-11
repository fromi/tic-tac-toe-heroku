angular.module('TicTacToe').controller('GameController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        var gameResource = $resource('/games/' + $routeParams.id);

        /**
         * @typedef {Object} Player
         * @property {String} name
         * @property {String} status
         * @property {Boolean} playing
         */
        /**
         * @typedef {Object} Game
         * @property {String} id
         * @property {String} state
         * @property {Array<Array<String>>} grid
         * @property {Object<string, Player>} players
         */
        $scope.game = gameResource.get();

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/game-started', function () {
            $scope.game = gameResource.get();
        });
    }
]);