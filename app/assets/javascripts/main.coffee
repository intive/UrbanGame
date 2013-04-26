app = angular.module('web', ['ui.bootstrap'])
angularUi = angular.module('angularUi', ['ui'])
app.config ['$locationProvider', ($location) ->
	$location.html5Mode(true);
]