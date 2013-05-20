app.controller 'menuCtrl', ['$scope', ($scope) ->
    $scope.location = window.location.pathname

    #$scope.$watch 'selectLanguage', (newVal) ->
        #langform.submit() if !_.isUndefined(newVal)

]