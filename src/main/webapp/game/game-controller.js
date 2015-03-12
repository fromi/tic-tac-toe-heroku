angular.module('TicTacToe').controller('GameController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        var gameResource = $resource('/games/' + $routeParams.id);

        /**
         * @typedef {Object} Mark
         * @enum {String}
         */
        $scope.mark = {X: "X", O: "O"};

        /**
         * @typedef {Object} Player
         * @property {Mark} id
         * @property {User} user
         * @property {Boolean} ready
         * @property {Boolean} online
         */
        /**
         * @typedef {Object} Game
         * @property {String} id
         * @property {String} state
         * @property {Object<String, Player>} players
         * @property {Array<Array<Mark>>} grid
         */
        $scope.game = gameResource.get();

        StompService.subscribe($scope, '/game/' + $routeParams.id + '/game-started', function () {
            $scope.game = gameResource.get();
        });
    }
]);