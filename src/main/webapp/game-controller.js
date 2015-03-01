angular.module('TicTacToe').controller('GameController', ['$scope', '$resource', '$routeParams', 'StompService', function($scope, $resource, $routeParams, StompService) {

    /**
     * @typedef {Object} Game
     * @property {string} id
     * @property {Array<User>} users
     */
    $scope.game = $resource('/game/' + $routeParams.id).get();

    StompService.subscribe($scope, '/game/' + $routeParams.id + '/joined', function (user) {
        $scope.game.users.push(user);
    });

    $scope.joinGame = function () {
        StompService.send('/game/' + $routeParams.id + '/join');
    }
}]);