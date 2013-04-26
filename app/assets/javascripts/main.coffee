app = angular.module('web', ['ui', 'ui.bootstrap'])
angularUi = angular.module('angularUi', ['ui'])

app.config ['$routeProvider', '$locationProvider', ($routeProvider, $location) ->

    $routeProvider.when('/', {template: 'index.scaml'})
    $routeProvider.when('/games/new', {template: 'newgame.scaml', controller: 'newGameCtrl'})
    $routeProvider.when('/games/my', {template: 'mygames.scaml', controller: 'myGamesCtrl'})
    $routeProvider.when('/games/archive')
]


app.controller 'menuCtrl', ['$scope', '$http', '$rootScope', '$location', ($scope, $http, $rootScope, $location) ->
    $rootScope.location = window.location.pathname

    $scope.$watch 'selectLanguage', (newVal) ->
        if !_.isUndefined(newVal)
            langform.submit()
]


