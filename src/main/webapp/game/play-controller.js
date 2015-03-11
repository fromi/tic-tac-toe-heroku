angular.module('TicTacToe').controller('GamePlayController', [
    '$scope', '$resource', '$routeParams', 'StompService',
    function ($scope, $resource, $routeParams, StompService) {
        /**
         * @param {Number} row
         * @param {Number} column
         */
        $scope.mark = function(row, column) {
            StompService.send('/game/' + $routeParams.id + "/mark", {}, JSON.stringify({row:row, column:column}));
        };

        /**
         * @typedef {Object} Cell
         * @property {Number} row
         * @property {Number} column
         */
        /**
         * @typedef {Object} CellMarked
         * @property {Cell} cell
         * @property {String} mark
         */
        StompService.subscribe($scope, '/game/' + $routeParams.id + '/cell-marked',
            /** @param {CellMarked} cellMarked */
            function (cellMarked) {
                $scope.game.grid[cellMarked.cell.row][cellMarked.cell.column] = cellMarked.mark;
            }
        );
    }
]);