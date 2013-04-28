app = angular.module('web', ['ui', 'ui.bootstrap'])
angularUi = angular.module('angularUi', ['ui'])

app.config ['$routeProvider', '$locationProvider', ($routeProvider, $location) ->

    $routeProvider.when('/', {template: 'index.scaml'})
    $routeProvider.when('/games/new', {template: 'newgame.scaml', controller: 'newGameCtrl'})
    $routeProvider.when('/games/my', {template: 'mygames.scaml', controller: 'myGamesCtrl'})
    $routeProvider.when('/games/archive')
]


