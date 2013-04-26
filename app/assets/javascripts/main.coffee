app = angular.module('web', ['ui.bootstrap'])
angularUi = angular.module('angularUi', ['ui'])


app.config ['$routeProvider', '$locationProvider', ($routeProvider, $location) ->

    $routeProvider.when('/', {template: 'index.scaml'})
    $routeProvider.when('/games/new', {template: 'newgame.scaml', controller: 'newGameCtrl'})
    $routeProvider.when('/games/my', {template: 'mygames.scaml', controller: 'myGamesCtrl'})
    $routeProvider.when('/games/archive')
]
###
app.directive 'activeLink', ['$location', ($location) ->
    return {
        restrict: 'A',
        link: (scope, element, attrs, controller) ->
            clazz = attrs.activeLink
            attrs.$observe 'href', (value) ->
                path = value.substring(1)
            #path = path.substring(1); //hack because path does bot return including hashbang
            scope.location = $location
            scope.$watch 'location.path()', (newPath) ->
                if (path == newPath)
                    element.addClass(clazz)
                else
                    element.removeClass(clazz)

    }
]###

app.controller 'topMenuCtrl', ['$scope', '$rootScope', '$location', ($scope, $rootScope, $location) ->
    $rootScope.location = window.location.pathname

    $scope.setRoute = (route) ->
        $location.path(route)
]


