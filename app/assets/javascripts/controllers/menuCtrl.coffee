app.controller 'menuCtrl', ['$scope', ($scope) ->
    $scope.location = window.location.pathname

    $scope.checkLocation = ->
        /\/my\/games\/\d+/gi.test($scope.location)

]