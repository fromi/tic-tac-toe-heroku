angular.module('TicTacToe', ['ngResource']).run(['$rootScope', '$resource', function($rootScope, $resource) {
    $rootScope.user = $resource('/user').get();
}]);