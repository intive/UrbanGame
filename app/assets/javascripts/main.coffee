app = angular.module('web', ['ui.bootstrap'])

app.config ['$locationProvider', ($location) ->
	$location.html5Mode(true);
]