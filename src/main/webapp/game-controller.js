angular.module('TicTacToe').controller('GameController', ['$scope', '$resource', '$routeParams', 'StompService', function($scope, $resource, $routeParams, StompService) {

    /**
     * @typedef {Object} PlayingUser
     * @property {String} name
     * @property {String} status
     */
    /**
     * @typedef {Object} Game
     * @property {String} id
     * @property {Object<string, PlayingUser>} playingUsers
     */
    $scope.game = $resource('/game/' + $routeParams.id).get();

    $scope.joinGame = function () {
        StompService.send('/game/' + $routeParams.id + '/join');
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-joined', function (user) {
        $scope.game.playingUsers[user.id] = {name:user.name, status:"REGISTERED"};
    });

    $scope.joined = function(user, game) {
        return game.playingUsers.hasOwnProperty(user.id);
    };

    $scope.ready = function () {
        StompService.send('/game/' + $routeParams.id + "/ready");
    };

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/player-status-changed', playerStatusChangedHandler);

    /**
     * @typedef {Object} PlayerStatusChanged
     * @property {String} playerId
     * @property {String} status
     */
    function playerStatusChangedHandler(playerStatusChanged) {
        $scope.game.playingUsers[playerStatusChanged.playerId].status = playerStatusChanged.status;
    }
}]);